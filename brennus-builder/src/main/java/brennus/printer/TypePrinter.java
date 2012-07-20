package brennus.printer;

import java.util.Collection;
import java.util.List;

import brennus.model.BinaryExpression;
import brennus.model.CallMethodExpression;
import brennus.model.CaseStatement;
import brennus.model.CastExpression;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.ExpressionVisitor;
import brennus.model.Field;
import brennus.model.FutureType;
import brennus.model.GetExpression;
import brennus.model.IfStatement;
import brennus.model.InstanceOfExpression;
import brennus.model.LiteralExpression;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.ReturnStatement;
import brennus.model.SetStatement;
import brennus.model.Statement;
import brennus.model.StatementVisitor;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;
import brennus.model.Type;
import brennus.model.TypeVisitor;
import brennus.model.UnaryExpression;

public class TypePrinter {

  public void print(Type type) {
    type.accept(new TypePrinterVisitor());
  }

}
class TypePrinterVisitor implements TypeVisitor, StatementVisitor {

  private int indent;

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

  private void printMethods(List<Method> methods) {
    for (Method m : methods) {
      println(
          getKeywords(m.getFlags()) + " " + m.getReturnType().getName() + " " + m.getName() + "("+paramString(m.getParameters())+") {"
          );
      incIndent();
      for (Statement s : m.getStatements()) {
        s.accept(this);
      }
      decIndent();
      println("}");
      println();
    }
  }

  private String paramString(List<Parameter> parameters) {
    String result = null;
    for (Parameter parameter : parameters) {
      if (result == null) {
        result = "";
      } else {
        result += ", ";
      }
      result += parameter.getType().getName()+" "+parameter.getName();
    }
    return result == null ? "" : result;
  }

  private String getKeywords(MemberFlags m) {
    return (m.isStatic() ? "static " : "") +
              m.getProtection().name().toLowerCase();
  }

  private void printFields(List<Field> fields) {
    for (Field f : fields) {
      println(
          getKeywords(f.getFlags()) + " " + f.getType().getName() + " " + f.getName() + ";"
          );
    }
  }

  // Types

  @Override
  public void visit(ExistingType existingType) {
    println(existingType.getExisting());
  }

  @Override
  public void visit(FutureType futureType) {
    println("class "+futureType.getName()+
        (futureType.getExtending() == null ? "" : " extends " + futureType.getExtending().getName())+
        " {");
    incIndent();
    println("// static fields");
    printFields(futureType.getStaticFields());
    println();
    println("// static methods");
    printMethods(futureType.getStaticMethods());
    println();
    println("// fields");
    printFields(futureType.getFields());
    println();
    println("// methods");
    printMethods(futureType.getMethods());
    println();
    decIndent();
    println("}");
  }

  // Statements

  @Override
  public void visit(ReturnStatement returnStatement) {
    println("return " + toString(returnStatement.getExpression())+";");
  }

  @Override
  public void visit(ThrowStatement throwStatement) {
    println("throw "+ toString(throwStatement.getExpression())+";");
  }

  @Override
  public void visit(ExpressionStatement expressionStatement) {
    println(toString(expressionStatement.getExpression())+";");
  }

  private String toString(Expression expression) {
    ExpressionStringifierVisitor expressionVisitor = new ExpressionStringifierVisitor();
    expression.accept(expressionVisitor);
    return expressionVisitor.toString();
  }

  @Override
  public void visit(SwitchStatement switchStatement) {
    println("switch (" + toString(switchStatement.getExpression()) + ") {");
    incIndent();
    Collection<CaseStatement> caseStatements = switchStatement.getCaseStatements();
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
  public void visit(CaseStatement caseStatement) {
    String caseType;
    if (caseStatement.getExpression() == null) {
      caseType = "default";
    } else {
      caseType = "case " + toString(caseStatement.getExpression());
    }
    println(caseType + ":");
    incIndent();
    List<Statement> statements = caseStatement.getStatements();
    for (Statement statement : statements) {
      statement.accept(this);
    }
    decIndent();
    println("break;");
  }

  @Override
  public void visit(SetStatement setStatement) {
    println(setStatement.getTo() + " = " + toString(setStatement.getExpression()) + ";");
  }

  @Override
  public void visit(IfStatement ifStatement) {
    println("if ("+toString(ifStatement.getExpression())+") {");
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
    List<Expression> parameters = callMethodExpression.getParameters();
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
    binaryExpression.getLeftExpression().accept(this);
    sb.append(" "+binaryExpression.getOperator().getRepresentation()+" ");
    binaryExpression.getRightExpression().accept(this);
  }

  @Override
  public String toString() {
    return sb.toString();
  }

  @Override
  public void visit(UnaryExpression unaryExpression) {
    sb.append(unaryExpression.getOperator().getRepresentation()).append(" ");
    unaryExpression.getExpression().accept(this);
  }

  @Override
  public void visit(InstanceOfExpression instanceOfExpression) {
    instanceOfExpression.getExpression().accept(this);
    sb.append(" instanceOf ").append(instanceOfExpression.getType().getName());
  }

  @Override
  public void visit(CastExpression castExpression) {
    sb.append("((").append(castExpression.getType().getName()).append(")");
    castExpression.getExpression().accept(this);
    sb.append(")");
  }

}
