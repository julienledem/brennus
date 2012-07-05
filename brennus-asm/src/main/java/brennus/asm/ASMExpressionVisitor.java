package brennus.asm;

import java.util.List;

import brennus.MethodContext;
import brennus.model.BinaryExpression;
import brennus.model.CallMethodExpression;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.ExpressionVisitor;
import brennus.model.Field;
import brennus.model.FieldAccessType;
import brennus.model.GetExpression;
import brennus.model.LiteralExpression;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.ParameterAccessType;
import brennus.model.Type;
import brennus.model.VarAccessType;
import brennus.model.VarAccessTypeVisitor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

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
    VarAccessType varAccessType = methodContext.getVarAccessType(getExpression.getFieldName());
    varAccessType.accept(new VarAccessTypeVisitor() {
      public void visit(FieldAccessType fieldAccessType) {
        Field field = fieldAccessType.getField();
        methodByteCodeContext.loadThis();
        methodByteCodeContext.addInstruction(new FieldInsnNode(GETFIELD, methodContext.getType().getClassIdentifier(), getExpression.getFieldName(), field.getSignature()));
        lastExpressionType = field.getType();
      }
      public void visit(ParameterAccessType parameterAccessType) {
        // TODO: type support
        //        System.out.println(getExpression.getFieldName()+" "+param.getIndex());
        Parameter param = parameterAccessType.getParam();
        // TODO: check boxing
        methodByteCodeContext.load(param.getType(), param.getIndex() + 1);
        lastExpressionType = param.getType();
      }
    });
  }

  @Override
  public void visit(CallMethodExpression callMethodExpression) {
//    System.out.println(callMethodExpression);
    methodByteCodeContext.loadThis();
    String methodName = callMethodExpression.getMethodName();
    // TODO: use parameter count/types for lookup
    Method method = getMethod(methodName);
    if (method != null) {
      List<Expression> parameterValues = callMethodExpression.getParameters();
      List<Parameter> parameterTypes = method.getParameters();
      if (parameterTypes.size() != parameterValues.size()) {
        throw new RuntimeException("parameters passed do not match, parameters declared in "+method);
      }
      for (int i = 0; i < parameterValues.size(); i++) {
        Expression expression = parameterValues.get(i);
        Type expected = parameterTypes.get(i).getType();
        expression.accept(this);
        // TODO: handle more than 1 parameter !!!
        // don't assume first on the stack
        methodByteCodeContext.handleConversion(lastExpressionType, expected);
      }
      //      System.out.println(method);
      methodByteCodeContext.addInstruction(new MethodInsnNode(INVOKEVIRTUAL, method.getTypeName(), methodName, method.getSignature()));
      lastExpressionType = method.getReturnType();
    } else {
      throw new RuntimeException("can't find method "+methodName+" in hierarchy of "+methodContext.getType());
    }
  }

  private Method getMethod(String methodName) {
    return methodContext.getType().getMethod(methodName);
  }

  @Override
  public void visit(LiteralExpression literalExpression) {
    lastExpressionType = literalExpression.getType();
    // TODO: support other types
    if (literalExpression.getType().getExisting().equals(Integer.TYPE)) {
      methodByteCodeContext.push(BIPUSH, ((Integer)literalExpression.getValue()).intValue());
    } else if (literalExpression.getType().getExisting().equals(String.class)) {
      methodByteCodeContext.ldc((String)literalExpression.getValue());
    }
  }

  @Override
  public void visit(BinaryExpression binaryExpression) {
    // TODO: support other types
    binaryExpression.getLeftExpression().accept(this);
    binaryExpression.getRightExpression().accept(this);
    lastExpressionType = ExistingType.INT;
    switch (binaryExpression.getOperator()) {
    case PLUS:
      methodByteCodeContext.addInstruction(new InsnNode(IADD));
      break;
    default:
      // TODO: other operators
      throw new UnsupportedOperationException("op: "+binaryExpression.getOperator());
    }
  }

  public Type getExpressionType() {
    return lastExpressionType;
  }

}
