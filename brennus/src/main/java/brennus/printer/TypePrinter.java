package brennus.printer;

import java.util.Collection;
import java.util.List;

import brennus.CallMethodExpression;
import brennus.CaseStatement;
import brennus.ExistingType;
import brennus.Expression;
import brennus.ExpressionStatement;
import brennus.ExpressionVisitor;
import brennus.Field;
import brennus.FutureType;
import brennus.GetExpression;
import brennus.LiteralExpression;
import brennus.MemberFlags;
import brennus.Method;
import brennus.Parameter;
import brennus.ReturnStatement;
import brennus.Statement;
import brennus.StatementVisitor;
import brennus.SwitchStatement;
import brennus.Type;
import brennus.TypeVisitor;


public class TypePrinter implements TypeVisitor, StatementVisitor, ExpressionVisitor {

  private int indent;

  public void print(Type type) {
    type.visit(this);
  }

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

  private void print(Object o) {
    System.out.print(o);
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
        s.visit(this);
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
    printIndent();
    print("return ");
    returnStatement.getExpression().visit(this);
    print(";");
    println();
  }

  @Override
  public void visit(ExpressionStatement expressionStatement) {
    printIndent();
    expressionStatement.getExpression().visit(this);
    print(";");
    println();
  }

  @Override
  public void visit(SwitchStatement switchStatement) {
    printIndent();
    print("switch (");
    switchStatement.getExpression().visit(this);
    print(") {");
    println();
    incIndent();
    Collection<CaseStatement> caseStatements = switchStatement.getCaseStatements();
    for (CaseStatement caseStatement : caseStatements) {
      caseStatement.visit(this);
    }
    decIndent();
    println("}");
  }

  @Override
  public void visit(CaseStatement caseStatement) {
    printIndent();
    Expression expression = caseStatement.getExpression();
    if (expression == null) {
      print("default");
    } else {
      print("case ");
      expression.visit(this);
    }
    print(":");
    println();
    incIndent();
    List<Statement> statements = caseStatement.getStatements();
    for (Statement statement : statements) {
      statement.visit(this);
    }
    decIndent();
    println("break;");
  }

  // Expressions

  @Override
  public void visit(LiteralExpression literalExpression) {
    print(literalExpression.getValue());

  }

  @Override
  public void visit(GetExpression getFieldExpression) {
    print(getFieldExpression.getFieldName());
  }

  @Override
  public void visit(CallMethodExpression callMethodExpression) {
    print("this."+callMethodExpression.getMethodName()+"()");
  }

}
