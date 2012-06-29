package brennus.asm;

import java.util.List;

import brennus.MethodContext;
import brennus.model.BoxingTypeConversion;
import brennus.model.CaseStatement;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.Field;
import brennus.model.FieldAccessType;
import brennus.model.LiteralExpression;
import brennus.model.Method;
import brennus.model.ParameterAccessType;
import brennus.model.PrimitiveType;
import brennus.model.ReturnStatement;
import brennus.model.SetStatement;
import brennus.model.Statement;
import brennus.model.StatementVisitor;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;
import brennus.model.Type;
import brennus.model.TypeConversion;
import brennus.model.TypeConversionVisitor;
import brennus.model.UnboxingTypeConversion;
import brennus.model.VarAccessTypeVisitor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

class ASMMethodGeneratorStatementVisitor implements Opcodes, StatementVisitor {

  private final MethodContext methodContext;
  private final MethodByteCodeContext methodByteCodeContext;

  private LabelNode currentLabel;

  public ASMMethodGeneratorStatementVisitor(MethodContext methodContext) {
    this.methodContext = methodContext;
    this.methodByteCodeContext = new MethodByteCodeContext(methodContext);
  }

  private Type visit(Expression expression) {
    ASMExpressionVisitor expressionVisitor = new ASMExpressionVisitor(methodContext, methodByteCodeContext);
    expression.accept(expressionVisitor);
    return expressionVisitor.getExpressionType();
  }

  @Override
  public void visit(ReturnStatement returnStatement) {
    Type expressionType = visit(returnStatement.getExpression());
    Type returnType = methodContext.getReturnType();
    methodByteCodeContext.handleConversion(expressionType, returnType);
    methodByteCodeContext.addInstruction(new InsnNode(ASMOps.getReturn(returnType)));
  }

  @Override
  public void visit(ExpressionStatement methodCallStatement) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public void visit(SwitchStatement switchStatement) {
    Type expressionType = visit(switchStatement.getExpression());
    List<CaseStatement> caseStatements = switchStatement.getCaseStatements();
    int[] values = new int[caseStatements.size()];
    LabelNode[] labels = new LabelNode[caseStatements.size()];
    for (int i = 0; i < labels.length; i++) {
      labels[i] = new LabelNode();
      values[i] = ((LiteralExpression)caseStatements.get(i).getExpression()).getValue();
    }
    LabelNode defaultLabel = new LabelNode();
    methodByteCodeContext.addInstruction(new LookupSwitchInsnNode(defaultLabel, values, labels));
    int i=0;
    for (CaseStatement caseStatement : caseStatements) {
      currentLabel = labels[i++];
      caseStatement.accept(this);
      currentLabel = null;
    }
    if (switchStatement.getDefaultCaseStatement() != null) {
      currentLabel = defaultLabel;
      switchStatement.getDefaultCaseStatement().accept(this);
      currentLabel = null;
    } else {
      methodByteCodeContext.addInstruction(defaultLabel);
    }
  }

  @Override
  public void visit(CaseStatement caseStatement) {
//    System.out.println(caseStatement);
    methodByteCodeContext.addInstruction(currentLabel);
    methodByteCodeContext.addInstruction(new FrameNode(F_SAME, 0, null, 0, null));
    List<Statement> statements = caseStatement.getStatements();
    for (Statement statement : statements) {
      statement.accept(this);
    }
  }

  @Override
  public void visit(ThrowStatement throwStatement) {
    Type expressionType = visit(throwStatement.getExpression());
    methodByteCodeContext.addInstruction(new InsnNode(ATHROW));
  }


  @Override
  public void visit(SetStatement setStatement) {
    methodByteCodeContext.load(ALOAD, 0);
    final Type expressionType = visit(setStatement.getExpression());
    methodContext.getVarAccessType(setStatement.getTo()).accept(
    new VarAccessTypeVisitor() {
      public void visit(ParameterAccessType parameterAccessType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

      }
      public void visit(FieldAccessType fieldAccessType) {
        Field field = fieldAccessType.getField();
        methodByteCodeContext.handleConversion(expressionType, field.getType());
//      mv.visitTypeInsn(CHECKCAST, "java/lang/String");
        methodByteCodeContext.addInstruction(new FieldInsnNode(PUTFIELD, methodContext.getClassIdentifier(), field.getName(), field.getSignature()));
      }
    });
  }


  public void addDefaultConstructorStatements() {
    methodByteCodeContext.load(ALOAD, 0);
    methodByteCodeContext.addInstruction(new MethodInsnNode(INVOKESPECIAL, methodContext.getType().getExtending().getClassIdentifier(), "<init>", "()V"));
    methodByteCodeContext.addInstruction(new InsnNode(RETURN));
  }

  public MethodNode getMethodNode() {
    return methodByteCodeContext.getMethodNode();
  }
}
