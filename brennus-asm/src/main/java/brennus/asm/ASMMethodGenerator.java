package brennus.asm;

import static brennus.model.ExistingType.INT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brennus.ImmutableList;
import brennus.LocalVarContext;
import brennus.MethodContext;
import brennus.model.CaseStatement;
import brennus.model.BinaryExpression;
import brennus.model.CallConstructorExpression;
import brennus.model.CallConstructorStatement;
import brennus.model.CallMethodExpression;
import brennus.model.CaseBlockStatement;
import brennus.model.CaseStatementVisitor;
import brennus.model.CastExpression;
import brennus.model.DefineVarStatement;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.ExpressionVisitor;
import brennus.model.Field;
import brennus.model.FieldAccessType;
import brennus.model.GetExpression;
import brennus.model.GotoCaseStatement;
import brennus.model.GotoStatement;
import brennus.model.IfStatement;
import brennus.model.InstanceOfExpression;
import brennus.model.LabelStatement;
import brennus.model.LiteralExpression;
import brennus.model.LocalVariableAccessType;
import brennus.model.Parameter;
import brennus.model.ParameterAccessType;
import brennus.model.ReturnStatement;
import brennus.model.SetStatement;
import brennus.model.Statement;
import brennus.model.StatementVisitor;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;
import brennus.model.Type;
import brennus.model.UnaryExpression;
import brennus.model.VarAccessTypeVisitor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

class ASMMethodGenerator implements Opcodes, StatementVisitor {

  private final MethodContext methodContext;
  private final MethodByteCodeContext methodByteCodeContext;

  private LabelNode currentLabel;
  private LabelNode endLabel;

  public ASMMethodGenerator(MethodContext methodContext) {
    this.methodContext = methodContext;
    this.methodByteCodeContext = new MethodByteCodeContext(methodContext);
  }

  private Type visit(Expression expression) {
    methodByteCodeContext.incIndent();
    ASMExpressionVisitor expressionVisitor = new ASMExpressionVisitor(methodContext, methodByteCodeContext);
    expression.accept(expressionVisitor);
    methodByteCodeContext.decIndent();
    return expressionVisitor.getExpressionType();
  }

  @Override
  public void visit(ReturnStatement returnStatement) {
    methodByteCodeContext.incIndent("return exp");
    Type expressionType = visit(returnStatement.getExpression());
    methodByteCodeContext.decIndent();
    Type returnType = methodContext.getReturnType();
    methodByteCodeContext.handleConversion(expressionType, returnType);
    methodByteCodeContext.addReturn(returnType);
  }

  @Override
  public void visit(ExpressionStatement methodCallStatement) {
    visit(methodCallStatement.getExpression());
  }

  @Override
  public void visit(SwitchStatement switchStatement) {
    methodByteCodeContext.incIndent("switch exp");
    Type expressionType = visit(switchStatement.getExpression());
    methodByteCodeContext.handleConversion(expressionType, INT, "switch(exp): convert exp to int");
    methodByteCodeContext.decIndent();
    List<CaseStatement> caseStatements = switchStatement.getCaseStatements();
    int minCase = Integer.MAX_VALUE;
    int maxCase = Integer.MIN_VALUE;
    Map<Integer, CaseStatement> cases = new HashMap<Integer, CaseStatement>();
    int[] values = new int[caseStatements.size()];
    final LabelNode[] labels = new LabelNode[caseStatements.size()];
    for (int i = 0; i < caseStatements.size(); i++) {
      CaseStatement caseStatement = caseStatements.get(i);
      int caseValue = ((Integer)((LiteralExpression)caseStatement.getExpression()).getValue()).intValue();
      values[i] = caseValue;
      minCase = Math.min(minCase, caseValue);
      maxCase = Math.max(caseValue, maxCase);
      cases.put(caseValue, caseStatement);
    }
    Arrays.sort(values);
    for (int i = 0; i < values.length; i++) {
      final int index = i;
      cases.get(values[i]).accept(new CaseStatementVisitor() {
        @Override
        public void visit(GotoCaseStatement gotoCaseStatement) {
          labels[index] = methodByteCodeContext.getLabelForSwitchGotoCase(gotoCaseStatement.getLabel());
        }
        @Override
        public void visit(CaseBlockStatement caseBlockStatement) {
          labels[index] = new LabelNode();
        }
      });
    }
    LabelNode defaultLabel = new LabelNode();
    endLabel = new LabelNode();
    // TODO: more cases
    if ((maxCase - minCase) == values.length - 1) {
      methodByteCodeContext.addInstruction(new TableSwitchInsnNode(minCase, maxCase, defaultLabel, labels), "switch(", switchStatement.getExpression(), ")");
    } else {
      methodByteCodeContext.addInstruction(new LookupSwitchInsnNode(defaultLabel, values, labels), "switch(", switchStatement.getExpression(), ")");
    }
    methodByteCodeContext.incIndent("switch");
    for (int i = 0; i < values.length; i++) {
      currentLabel = labels[i];
      cases.get(values[i]).accept(this);
      currentLabel = null;
    }
    if (switchStatement.getDefaultCaseStatement() != null) {
      currentLabel = defaultLabel;
      switchStatement.getDefaultCaseStatement().accept(this);
      currentLabel = null;
    } else {
      methodByteCodeContext.addInstruction(defaultLabel);
    }
    methodByteCodeContext.decIndent();
    methodByteCodeContext.addInstruction(endLabel, "switch end");
    endLabel = null;
  }

  @Override
  public void visit(CaseBlockStatement caseStatement) {
    Object value = caseStatement.getExpression() == null ? "default" : caseStatement.getliteralExpression().getValue();
    methodByteCodeContext.addLabel(caseStatement.getLine(), currentLabel, "case", value);
    // TODO: understand Frame ... :(
    // I use COMPUTE_FRAME in the generator
    //methodByteCodeContext.addInstruction(new FrameNode(F_SAME, 0, null, 0, null), "case", value);
    methodByteCodeContext.incIndent("case", value);
    for (Statement statement : caseStatement.getStatements()) {
      this.visit(statement);
    }
    if (caseStatement.isBreakCase()) {
      methodByteCodeContext.addInstruction(new JumpInsnNode(GOTO, endLabel), "break case");
    }
    methodByteCodeContext.decIndent();
  }

  @Override
  public void visit(GotoCaseStatement gotoCaseStatement) {
    // nothing to do
  }

  @Override
  public void visit(ThrowStatement throwStatement) {
    methodByteCodeContext.incIndent("throw exp");
    Type expressionType = visit(throwStatement.getExpression());
    methodByteCodeContext.decIndent();
    methodByteCodeContext.addInstruction(new InsnNode(ATHROW));
  }


  @Override
  public void visit(final SetStatement setStatement) {
    methodContext.getVarAccessType(setStatement.getTo()).accept(
    new VarAccessTypeVisitor() {
      private Type evalExp() {
        methodByteCodeContext.incIndent("set exp", setStatement.getTo());
        Type expressionType = ASMMethodGenerator.this.visit(setStatement.getExpression());
        methodByteCodeContext.decIndent();
        return expressionType;
      }
      public void visit(ParameterAccessType parameterAccessType) {
        Type expressionType = evalExp();
        // TODO: type support
        //        System.out.println(getExpression.getFieldName()+" "+param.getIndex());
        Parameter param = parameterAccessType.getParam();
        // TODO: long and double take 2 slots
        // TODO: check boxing
        methodByteCodeContext.store(
            param.getType(),
            // TODO: static methods don't have this
            methodByteCodeContext.getParamByteCodeIndex(param.getIndex()),
            "set param", setStatement.getTo());
      }
      public void visit(FieldAccessType fieldAccessType) {
        methodByteCodeContext.loadThis("set", setStatement.getTo());
        Type expressionType = evalExp();
        Field field = fieldAccessType.getField();
        methodByteCodeContext.handleConversion(expressionType, field.getType());
        methodByteCodeContext.addInstruction(new FieldInsnNode(PUTFIELD, methodContext.getClassIdentifier(), field.getName(), field.getSignature()), "set", setStatement.getTo());
      }
      public void visit(LocalVariableAccessType localVariableAccessType) {
        Type expressionType = evalExp();
        // TODO: type support
        // TODO: long and double take 2 slots
        methodByteCodeContext.store(
            expressionType,
            methodByteCodeContext.getLocalVariableByteCodeIndex(localVariableAccessType.getVarIndex()),
            "set local var", setStatement.getTo());
      }
    });
  }

  public MethodNode getMethodNode() {
    return methodByteCodeContext.getMethodNode();
  }

  public void visit(Statement statement) {
    methodByteCodeContext.addLineNumber(statement.getLine());
    methodByteCodeContext.incIndent(statement.getClass().getSimpleName());
    statement.accept(this);
    methodByteCodeContext.decIndent();
  }

  @Override
  public void visit(final IfStatement ifStatement) {
    methodByteCodeContext.incIndent("if");
    ifStatement.getExpression().accept(new ExpressionVisitor() {
      public void visit(BinaryExpression binaryExpression) {
        methodByteCodeContext.incIndent("if left");
        ASMMethodGenerator.this.visit(binaryExpression.getLeftExpression());
        methodByteCodeContext.decIndent();
        methodByteCodeContext.incIndent("if right");
        ASMMethodGenerator.this.visit(binaryExpression.getRightExpression());
        methodByteCodeContext.decIndent();
        methodByteCodeContext.incIndent(binaryExpression.getOperator().getRepresentation());
        // TODO: combine expression ! and =  etc
        switch (binaryExpression.getOperator()) {
        case EQUALS:
          // TODO handle types
          generateThenElse(IF_ICMPNE, ifStatement.getElseStatements(), ifStatement.getThenStatements());
          break;
        case GREATER_THAN:
          generateThenElse(IF_ICMPGT, ifStatement.getThenStatements(), ifStatement.getElseStatements());
          break;
        default:
          throw new UnsupportedOperationException("op: "+binaryExpression.getOperator());
        }
        methodByteCodeContext.decIndent();
      }

      public void visit(LiteralExpression literalExpression) {
        throw new UnsupportedOperationException();
      }

      public void visit(CallMethodExpression callMethodExpression) {
        methodByteCodeContext.incIndent("if call", callMethodExpression.getMethodName());
        ASMMethodGenerator.this.visit(callMethodExpression);
        // if (false) else then
        generateThenElse(IFEQ, ifStatement.getElseStatements(), ifStatement.getThenStatements());
        methodByteCodeContext.decIndent();
      }

      public void visit(GetExpression getFieldExpression) {
        methodByteCodeContext.incIndent("if get", getFieldExpression.getFieldName());
        ASMMethodGenerator.this.visit(getFieldExpression);
        // if (false) else then
        generateThenElse(IFEQ, ifStatement.getElseStatements(), ifStatement.getThenStatements());
        methodByteCodeContext.decIndent();
      }

      @Override
      public void visit(UnaryExpression unaryExpression) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void visit(InstanceOfExpression instanceOfExpression) {
        methodByteCodeContext.incIndent("if instanceof", instanceOfExpression.getType());
        ASMMethodGenerator.this.visit(instanceOfExpression.getExpression());
        methodByteCodeContext.addInstruction(new TypeInsnNode(INSTANCEOF, instanceOfExpression.getType().getClassIdentifier()), "if");
        generateThenElse(IFEQ, ifStatement.getElseStatements(), ifStatement.getThenStatements());
        methodByteCodeContext.decIndent();
      }

      @Override
      public void visit(CastExpression castExpression) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void visit(CallConstructorExpression callConstructorExpression) {
        throw new UnsupportedOperationException();
      }

    });
    methodByteCodeContext.decIndent();
  }

  private void generateThenElse(int jumpInst, ImmutableList<Statement> thenStatements, ImmutableList<Statement> elseStatements) {
    LabelNode thenNode = new LabelNode();
    LabelNode endNode = new LabelNode();
    methodByteCodeContext.addInstruction(new JumpInsnNode(jumpInst, thenNode), "IF exp GOTO label else keep going");
    methodByteCodeContext.incIndent();
    // else
    for (Statement statement : elseStatements) {
      visit(statement);
    }
    methodByteCodeContext.decIndent();
    methodByteCodeContext.addInstruction(new JumpInsnNode(GOTO, endNode), "endElse now Then");
    // then
    methodByteCodeContext.addLabel(thenNode, "then");
    methodByteCodeContext.incIndent();
    for (Statement statement : thenStatements) {
      visit(statement);
    }
    methodByteCodeContext.decIndent();
    methodByteCodeContext.addLabel(endNode, "endThen");
  }

  @Override
  public void visit(LabelStatement labelStatement) {
    methodByteCodeContext.addNamedLabel(labelStatement.getLine(), labelStatement.getName());
  }

  @Override
  public void visit(GotoStatement gotoStatement) {
    methodByteCodeContext.gotoLabel(gotoStatement.getName());
  }

  @Override
  public void visit(CallConstructorStatement callConstructorStatement) {
    visit(callConstructorStatement.getExpression());
  }

  @Override
  public void visit(DefineVarStatement defineVarStatement) {
    LocalVarContext context = methodContext.defineLocalVar(defineVarStatement.getType(), defineVarStatement.getVarName());
    methodByteCodeContext.defineLocalVar(context.getType(), context.getName(), context.getIndex());
  }

}