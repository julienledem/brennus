package brennus.asm;

import brennus.MethodContext;
import brennus.model.AddExpression;
import brennus.model.CallMethodExpression;
import brennus.model.ExistingType;
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
import org.objectweb.asm.tree.IntInsnNode;
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
        methodByteCodeContext.load(ALOAD, 0);
        methodByteCodeContext.addInstruction(new FieldInsnNode(GETFIELD, methodContext.getType().getClassIdentifier(), getExpression.getFieldName(), field.getSignature()));
        lastExpressionType = field.getType();
      }
      public void visit(ParameterAccessType parameterAccessType) {
        // TODO: type support
        //        System.out.println(getExpression.getFieldName()+" "+param.getIndex());
        Parameter param = parameterAccessType.getParam();
        // TODO: check boxing
        methodByteCodeContext.load(ASMOps.getLoad(param.getType()), param.getIndex() + 1);
        lastExpressionType = param.getType();
      }
    });
  }

  @Override
  public void visit(CallMethodExpression callMethodExpression) {
//    System.out.println(callMethodExpression);
    methodByteCodeContext.load(ALOAD, 0);
    String methodName = callMethodExpression.getMethodName();
    Method method = getMethod(methodName);
    if (method != null) {
//      System.out.println(method);
      methodByteCodeContext.addInstruction(new MethodInsnNode(INVOKEVIRTUAL, methodContext.getType().getClassIdentifier(), methodName, method.getSignature()));
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
    // TODO: support other types
    lastExpressionType = ExistingType.INT;
    methodByteCodeContext.addInstruction(new IntInsnNode(BIPUSH, literalExpression.getValue()));
  }

  @Override
  public void visit(AddExpression addExpression) {
    // TODO: support other types
    addExpression.getLeftExpression().accept(this);
    addExpression.getRightExpression().accept(this);
    lastExpressionType = ExistingType.INT;
    methodByteCodeContext.addInstruction(new InsnNode(IADD));
  }

  public Type getExpressionType() {
    return lastExpressionType;
  }

}
