package brennus.printer;

import brennus.MethodContext;
import brennus.model.BinaryExpression;
import brennus.model.CallConstructorExpression;
import brennus.model.CallConstructorStatement;
import brennus.model.CallMethodExpression;
import brennus.model.CaseBlockStatement;
import brennus.model.CaseStatement;
import brennus.model.CastExpression;
import brennus.model.DefineVarStatement;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.ExpressionVisitor;
import brennus.model.Field;
import brennus.model.FieldAccessType;
import brennus.model.FutureType;
import brennus.model.GetExpression;
import brennus.model.GotoCaseStatement;
import brennus.model.GotoStatement;
import brennus.model.IfStatement;
import brennus.model.InstanceOfExpression;
import brennus.model.InstantiationExpression;
import brennus.model.LabelStatement;
import brennus.model.LiteralExpression;
import brennus.model.LocalVariableAccessType;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.NewArrayExpression;
import brennus.model.Parameter;
import brennus.model.ParameterAccessType;
import brennus.model.ReturnStatement;
import brennus.model.SetStatement;
import brennus.model.Statement;
import brennus.model.StatementVisitor;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;
import brennus.model.Type;
import brennus.model.TypeVisitor;
import brennus.model.UnaryExpression;
import brennus.model.VarAccessType;
import brennus.model.VarAccessTypeVisitor;

public class TypePrinter {

  public void print(Type type) {
    type.accept(new TypePrinterVisitor());
  }

}
class TypePrinterVisitor implements TypeVisitor, StatementVisitor {

  private int indent;
  private MethodContext context;

  private void printIndent() {
    for (int i = 0; i < indent; i++) {
      System.out.print(" ");

    }
  }

  private void println() {
    System.out.println();
  }

  private void println(Object ln) {
    printIndent();
    System.out.println(ln);
  }

  private void decIndent() {
    indent -= 2;
  }

  private void incIndent() {
    indent += 2;
  }

  private void printMethods(FutureType type, String typeName, Iterable<Method> methods) {
    for (Method m : methods) {
      context = new MethodContext(type, m);
      String methodName = m.getName();
      String returnType = m.getReturnType().getPrintableName();
      if (methodName.equals("<init>")) {
        // constructor
        println(
            getKeywords(m.getFlags()) + " " + typeName + "("+paramString(m.getParameters())+") {"
            );
      } else {
        println(
            getKeywords(m.getFlags()) + " " + returnType + " " + methodName + "("+paramString(m.getParameters())+") {"
            );
      }
      incIndent();
      for (Statement s : m.getStatements()) {
        s.accept(this);
      }
      decIndent();
      println("}");
      println();
    }
  }

  private String paramString(Iterable<Parameter> parameters) {
    String result = null;
    for (Parameter parameter : parameters) {
      if (result == null) {
        result = "";
      } else {
        result += ", ";
      }
      result += parameter.getType().getPrintableName()+" "+parameter.getName();
    }
    return result == null ? "" : result;
  }

  private String getKeywords(MemberFlags m) {
    return (m.isStatic() ? "static " : "") +
              m.getProtection().name().toLowerCase();
  }

  private void printFields(Iterable<Field> fields) {
    for (Field f : fields) {
      println(
          getKeywords(f.getFlags()) + " " + f.getType().getPrintableName() + " " + f.getName() + ";"
          );
    }
  }

  // Types

  @Override
  public void visit(ExistingType existingType) {
    printIndent();
    println(existingType.getPrintableName());
  }

  @Override
  public void visit(FutureType futureType) {
    int lastDot = futureType.getName().lastIndexOf('.');
    String name;
    if (lastDot == -1) {
      name = futureType.getPrintableName();
    } else {
      name = futureType.getPrintableName().substring(lastDot + 1);
      println("package "+futureType.getName().substring(0, lastDot)+";");
    }
    println("class " + name +
        (futureType.getExtending() == null ? "" : " extends " + futureType.getExtending().getPrintableName())+
        " {");
    incIndent();
    println("// static fields");
    printFields(futureType.getStaticFields());
    println();
    println("// static methods");
    printMethods(futureType, name, futureType.getStaticMethods());
    println();
    println("// fields");
    printFields(futureType.getFields());
    println();
    println("// constructors");
    printMethods(futureType, name, futureType.getConstructors());
    println();
    println("// methods");
    printMethods(futureType, name, futureType.getMethods());
    println();
    decIndent();
    println("}");
  }

  // Statements

  @Override
  public void visit(ReturnStatement returnStatement) {
    println("return " + toString(returnStatement.getExpression())+"; // line " + returnStatement.getLine());
  }

  @Override
  public void visit(ThrowStatement throwStatement) {
    println("throw "+ toString(throwStatement.getExpression())+"; // line " + throwStatement.getLine());
  }

  @Override
  public void visit(ExpressionStatement expressionStatement) {
    println(toString(expressionStatement.getExpression())+"; // line " + expressionStatement.getLine());
  }

  private String toString(Expression expression) {
    ExpressionStringifierVisitor expressionVisitor = new ExpressionStringifierVisitor();
    expression.accept(expressionVisitor);
    return expressionVisitor.toString();
  }

  @Override
  public void visit(SwitchStatement switchStatement) {
    println("switch (" + toString(switchStatement.getExpression()) + ") { // line " + switchStatement.getLine());
    incIndent();
    Iterable<CaseStatement> caseStatements = switchStatement.getCaseStatements();
    for (CaseStatement caseStatement : caseStatements) {
      caseStatement.accept(this);
    }
    if (switchStatement.getDefaultCaseStatement() != null) {
      switchStatement.getDefaultCaseStatement().accept(this);
    }
    decIndent();
    println("}");
  }

  @Override
  public void visit(CaseBlockStatement caseStatement) {
    String caseType;
    if (caseStatement.getExpression() == null) {
      caseType = "default";
    } else {
      caseType = "case " + toString(caseStatement.getExpression());
    }
    println(caseType + ": // line " + caseStatement.getLine());
    incIndent();
    Iterable<Statement> statements = caseStatement.getStatements();
    for (Statement statement : statements) {
      statement.accept(this);
    }
    decIndent();
    if (caseStatement.isBreakCase()) {
      println("break;");
    }
  }

  @Override
  public void visit(final SetStatement setStatement) {
    VarAccessType varAccessType = context.getVarAccessType(setStatement.getTo());
    varAccessType.accept(new VarAccessTypeVisitor() {
      public void visit(LocalVariableAccessType localVariableAccessType) {
        print("", " // local variable");
      }
      public void visit(ParameterAccessType parameterAccessType) {
        print("", " // parameter");
      }
      public void visit(FieldAccessType fieldAccessType) {
        print("this.", " // field");
      }
      private void print(String prefix, String suffix) {
        println(
            prefix +
            setStatement.getTo() +
            " = " +
            TypePrinterVisitor.this.toString(setStatement.getExpression()) +
            ";" +
            suffix+" line "+setStatement.getLine());
      }
    });

  }

  @Override
  public void visit(IfStatement ifStatement) {
    println("if ("+toString(ifStatement.getExpression())+") { // line " + ifStatement.getLine());
    incIndent();
    for (Statement statement : ifStatement.getThenStatements()) {
      statement.accept(this);
    }
    decIndent();
    if (ifStatement.getElseStatements().size() > 0) {
      println("} else {");
      incIndent();
      for (Statement statement : ifStatement.getElseStatements()) {
        statement.accept(this);
      }
      decIndent();
    }
    println("}");
  }

  @Override
  public void visit(LabelStatement labelStatement) {
    println(labelStatement.getName() + ": // line " + labelStatement.getLine());
  }

  @Override
  public void visit(GotoStatement gotoStatement) {
    println("goto " + gotoStatement.getName() + "; // line " + gotoStatement.getLine());
  }

  @Override
  public void visit(CallConstructorStatement callConstructorStatement) {
    println(toString(callConstructorStatement.getExpression())+"; // line " + callConstructorStatement.getLine());
  }

  @Override
  public void visit(DefineVarStatement defineVarStatement) {
    context.defineLocalVar(defineVarStatement.getType(), defineVarStatement.getVarName());
    println(defineVarStatement.getType().getName() + " " + defineVarStatement.getVarName() + "; // line " + defineVarStatement.getLine());
  }

  @Override
  public void visit(GotoCaseStatement gotoCaseStatement) {
    String caseType;
    if (gotoCaseStatement.getExpression() == null) {
      caseType = "default";
    } else {
      caseType = "case " + toString(gotoCaseStatement.getExpression());
    }
    println(caseType + ": // line " + gotoCaseStatement.getLine() + " goto case");
    incIndent();
      println("goto "+gotoCaseStatement.getLabel()+"; // actually directly jumping to this label");
    decIndent();
  }

}
class ExpressionStringifierVisitor implements ExpressionVisitor {

  private StringBuilder sb = new StringBuilder();

  @Override
  public void visit(LiteralExpression literalExpression) {
    if (literalExpression.getType().equals(ExistingType.STRING)) {
      sb.append("\"");
    }
    sb.append(literalExpression.getValue());
    if (literalExpression.getType().equals(ExistingType.STRING)) {
      sb.append("\"");
    }
  }

  @Override
  public void visit(GetExpression getFieldExpression) {
    sb.append(getFieldExpression.getFieldName());
  }

  @Override
  public void visit(CallMethodExpression callMethodExpression) {
    if (callMethodExpression.getCallee() == null){
      sb.append("this");
    } else {
      callMethodExpression.getCallee().accept(this);
    }
    sb.append("."+callMethodExpression.getMethodName()+"(");
    boolean first = true;
    Iterable<Expression> parameters = callMethodExpression.getParameters();
    for (Expression expression : parameters) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      expression.accept(this);
    }
    sb.append(")");
  }

  @Override
  public void visit(BinaryExpression binaryExpression) {
    switch (binaryExpression.getOperator()) {
    case GETARRAYATINDEX:
      binaryExpression.getLeftExpression().accept(this);
      sb.append("[");
      binaryExpression.getRightExpression().accept(this);
      sb.append("]");
      break;
    default:
      sb.append("(");
      binaryExpression.getLeftExpression().accept(this);
      sb.append(" "+binaryExpression.getOperator().getRepresentation()+" ");
      binaryExpression.getRightExpression().accept(this);
      sb.append(")");
      break;
    }
  }

  @Override
  public String toString() {
    return sb.toString();
  }

  @Override
  public void visit(UnaryExpression unaryExpression) {
    switch (unaryExpression.getOperator()) {
    case ARRAYSIZE:
      unaryExpression.getExpression().accept(this);
      sb.append(".length");
      break;
    case ISNULL:
      unaryExpression.getExpression().accept(this);
      sb.append(" == null");
      break;
    case ISNOTNULL:
      unaryExpression.getExpression().accept(this);
      sb.append(" != null");
      break;
    default:
      sb.append("(");
      sb.append(unaryExpression.getOperator().getRepresentation()).append(" ");
      unaryExpression.getExpression().accept(this);
      sb.append(")");
      break;
    }
  }

  @Override
  public void visit(InstanceOfExpression instanceOfExpression) {
    sb.append("(");
    instanceOfExpression.getExpression().accept(this);
    sb.append(" instanceOf ").append(instanceOfExpression.getType().getName());
    sb.append(")");
  }

  @Override
  public void visit(CastExpression castExpression) {
    sb.append("((").append(castExpression.getType().getName()).append(")");
    castExpression.getExpression().accept(this);
    sb.append(")");
  }

  @Override
  public void visit(CallConstructorExpression callConstructorExpression) {
    sb.append("super(");
    boolean first = true;
    Iterable<Expression> parameters = callConstructorExpression.getParameters();
    for (Expression expression : parameters) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      expression.accept(this);
    }
    sb.append(")");
  }

  @Override
  public void visit(InstantiationExpression instantiationExpression) {
    sb.append("new ").append(instantiationExpression.getType().getName()).append("(");
    boolean first = true;
    Iterable<Expression> parameters = instantiationExpression.getParameters();
    for (Expression expression : parameters) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      expression.accept(this);
    }
    sb.append(")");
  }

  @Override
  public void visit(NewArrayExpression e) {
    sb.append("new ").append(e.getType().getName()).append("[");
    e.getSize().accept(this);
    sb.append("]");
  }

}
