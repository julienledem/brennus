package brennus.asm;

import java.util.List;

import brennus.MethodContext;
import brennus.model.BinaryExpression;
import brennus.model.CallMethodExpression;
import brennus.model.CastExpression;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.ExpressionVisitor;
import brennus.model.Field;
import brennus.model.FieldAccessType;
import brennus.model.GetExpression;
import brennus.model.InstanceOfExpression;
import brennus.model.LiteralExpression;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.ParameterAccessType;
import brennus.model.Type;
import brennus.model.UnaryExpression;
import brennus.model.VarAccessType;
import brennus.model.VarAccessTypeVisitor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

class ASMExpressionVisitor implements Opcodes, ExpressionVisitor {

  private final MethodContext methodContext;
  private final MethodByteCodeContext methodByteCodeContext;

  private Type lastExpressionType;

  ASMExpressionVisitor(MethodContext methodContext, MethodByteCodeContext methodByteCodeContext) {
    super();
    this.methodContext = methodContext;
    this.methodByteCodeContext = methodByteCodeContext;
  }

  @Override
  public void visit(final GetExpression getExpression) {
    methodByteCodeContext.incIndent("get", getExpression.getFieldName());
    VarAccessType varAccessType = methodContext.getVarAccessType(getExpression.getFieldName());
    varAccessType.accept(new VarAccessTypeVisitor() {
      public void visit(FieldAccessType fieldAccessType) {
        Field field = fieldAccessType.getField();
        methodByteCodeContext.loadThis("get field", getExpression.getFieldName(), "on this");
        methodByteCodeContext.addInstruction(
            new FieldInsnNode(GETFIELD, methodContext.getType().getClassIdentifier(), getExpression.getFieldName(), field.getSignature()),
            "get field", getExpression.getFieldName());
        lastExpressionType = field.getType();
      }
      public void visit(ParameterAccessType parameterAccessType) {
        // TODO: type support
        //        System.out.println(getExpression.getFieldName()+" "+param.getIndex());
        Parameter param = parameterAccessType.getParam();
        // TODO: check boxing
        methodByteCodeContext.load(param.getType(), param.getIndex() + 1,
            "get param", getExpression.getFieldName());
        lastExpressionType = param.getType();
      }
    });
    methodByteCodeContext.decIndent();
  }

  @Override
  public void visit(CallMethodExpression callMethodExpression) {
    String methodName = callMethodExpression.getMethodName();
    methodByteCodeContext.incIndent("call method", methodName);
    Method method;
    if (callMethodExpression.getCallee()==null) {
      methodByteCodeContext.loadThis("calling on this", methodName);
      // TODO: use parameter count/types for lookup
      method = methodContext.getType().getMethod(methodName);
      if (method == null) {
        throw new RuntimeException("can't find method "+methodName+" in hierarchy of "+methodContext.getType());
      }
    } else {
      callMethodExpression.getCallee().accept(this);
      method = lastExpressionType.getMethod(methodName);
      if (method == null) {
        throw new RuntimeException("can't find method "+methodName+" in hierarchy of "+lastExpressionType);
      }
    }
    methodByteCodeContext.incIndent("pass", callMethodExpression.getParameters().size(), "params to", methodName);
    List<Expression> parameterValues = callMethodExpression.getParameters();
    List<Parameter> parameterTypes = method.getParameters();
    if (parameterTypes.size() != parameterValues.size()) {
      throw new RuntimeException("parameters passed do not match, parameters declared in "+method);
    }
    for (int i = 0; i < parameterValues.size(); i++) {
      methodByteCodeContext.incIndent("param", i);
      Expression expression = parameterValues.get(i);
      Type expected = parameterTypes.get(i).getType();
      expression.accept(this);
      methodByteCodeContext.handleConversion(lastExpressionType, expected, "param", i, "for", callMethodExpression.getMethodName());
      methodByteCodeContext.decIndent();
    }
    methodByteCodeContext.decIndent();
    methodByteCodeContext.addInstruction(new MethodInsnNode(INVOKEVIRTUAL, method.getTypeName(), methodName, method.getSignature()), "call", methodName);
    lastExpressionType = method.getReturnType();
    methodByteCodeContext.decIndent();
  }

  @Override
  public void visit(LiteralExpression literalExpression) {
    lastExpressionType = literalExpression.getType();
    // TODO: support other types
    if (literalExpression.getType().getExisting().equals(Integer.TYPE)) {
      methodByteCodeContext.push(BIPUSH, ((Integer)literalExpression.getValue()).intValue(), "int literal", literalExpression.getValue());
    } else if (literalExpression.getType().getExisting().equals(String.class)) {
      methodByteCodeContext.ldc((String)literalExpression.getValue(), "String literal", literalExpression.getValue());
    } else if (literalExpression.getType().getExisting().equals(Boolean.TYPE)) {
      boolean b = (Boolean)literalExpression.getValue();
      if (b) {
        methodByteCodeContext.addIConst1("bool literal", literalExpression.getValue());
      } else {
        methodByteCodeContext.addIConst0("bool literal", literalExpression.getValue());
      }
    } else {
      throw new UnsupportedOperationException(literalExpression.toString());
    }
  }

  @Override
  public void visit(BinaryExpression binaryExpression) {
    methodByteCodeContext.incIndent(binaryExpression.getOperator().getRepresentation());
    // TODO: support other types
    switch (binaryExpression.getOperator()) {
    case PLUS:
      methodByteCodeContext.incIndent("left +");
      binaryExpression.getLeftExpression().accept(this);
      methodByteCodeContext.decIndent();
      methodByteCodeContext.incIndent("+ right");
      binaryExpression.getRightExpression().accept(this);
      methodByteCodeContext.decIndent();
      lastExpressionType = ExistingType.INT;
      methodByteCodeContext.addInstruction(new InsnNode(IADD), "+");
      break;
    case AND:
      methodByteCodeContext.incIndent("left &&");
      binaryExpression.getLeftExpression().accept(this);
      methodByteCodeContext.decIndent();
      new LiteralExpression(false).accept(this);
      LabelNode falseLabel = new LabelNode();
      LabelNode endLabel = new LabelNode();
      methodByteCodeContext.addInstruction(new JumpInsnNode(IF_ICMPEQ, falseLabel), "AND: IF left is false => false");
      methodByteCodeContext.incIndent("&& right");
      binaryExpression.getRightExpression().accept(this);
      methodByteCodeContext.decIndent();
      new LiteralExpression(false).accept(this);
      methodByteCodeContext.addInstruction(new JumpInsnNode(IF_ICMPEQ, falseLabel), "AND: IF right is false => false");
      new LiteralExpression(true).accept(this);
      methodByteCodeContext.addInstruction(new JumpInsnNode(GOTO, endLabel), "AND: all true => skip false");
      methodByteCodeContext.addLabel(falseLabel, "AND: false");
      new LiteralExpression(false).accept(this);
      methodByteCodeContext.addLabel(endLabel, "AND: end");
      break;
    default:
      // TODO: other operators
      throw new UnsupportedOperationException("op: "+binaryExpression.getOperator());
    }
    methodByteCodeContext.decIndent();
  }

  public Type getExpressionType() {
    return lastExpressionType;
  }

  @Override
  public void visit(UnaryExpression unaryExpression) {
    methodByteCodeContext.incIndent(unaryExpression.getOperator().getRepresentation());
    methodByteCodeContext.incIndent("!exp");
    unaryExpression.getExpression().accept(this);
    methodByteCodeContext.decIndent();
    switch (unaryExpression.getOperator()) {
    case NOT:
      // TODO: combine with parent
      LabelNode l1 = new LabelNode();
      LabelNode l2 = new LabelNode();
      methodByteCodeContext.addInstruction(new JumpInsnNode(IFEQ, l1), "NOT: IF false => true");// if equal to 0 jump to L1
      methodByteCodeContext.addIConst0("NOT: result false");
      methodByteCodeContext.addInstruction(new JumpInsnNode(GOTO, l2), "NOT: jump to end");
      methodByteCodeContext.addLabel(l1, "NOT: true label");
      methodByteCodeContext.addIConst1("NOT: result true");
      methodByteCodeContext.addLabel(l2, "NOT: end label");
      break;
    default:
      // TODO: other operators
      throw new UnsupportedOperationException("op: "+unaryExpression.getOperator());
    }
    methodByteCodeContext.decIndent();
  }

  @Override
  public void visit(InstanceOfExpression instanceOfExpression) {
    methodByteCodeContext.incIndent("instanceOF");
    instanceOfExpression.getExpression().accept(this);
    methodByteCodeContext.addInstruction(new TypeInsnNode(INSTANCEOF, instanceOfExpression.getType().getClassIdentifier()));
    methodByteCodeContext.decIndent();
  }

  @Override
  public void visit(CastExpression castExpression) {
    methodByteCodeContext.incIndent("cast (", castExpression.getType(), ")");
    castExpression.getExpression().accept(this);
    methodByteCodeContext.addInstruction(new TypeInsnNode(CHECKCAST, castExpression.getType().getClassIdentifier()), "cast to", castExpression.getType());
    lastExpressionType = castExpression.getType();
    methodByteCodeContext.decIndent();
  }

}
