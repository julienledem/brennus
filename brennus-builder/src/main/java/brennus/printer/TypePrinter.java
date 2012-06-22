package brennus.printer;

import java.util.Collection;
import java.util.List;

import brennus.model.CallMethodExpression;
import brennus.model.CaseStatement;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.ExpressionVisitor;
import brennus.model.Field;
import brennus.model.FutureType;
import brennus.model.GetExpression;
import brennus.model.LiteralExpression;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.ReturnStatement;
import brennus.model.Statement;
import brennus.model.StatementVisitor;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;
import brennus.model.Type;
import brennus.model.TypeVisitor;


public class TypePrinter {

  public void print(Type type) {
    type.accept(new TypePrinterVisitor());
  }

}
class TypePrinterVisitor implements TypeVisitor, StatementVisitor, ExpressionVisitor {

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
    printIndent();
    print("return ");
    returnStatement.getExpression().accept(this);
    print(";");
    println();
  }

  @Override
  public void visit(ThrowStatement throwStatement) {
    printIndent();
    print("throw ");
    throwStatement.getExpression().accept(this);
    print(";");
    println();
  }

  @Override
  public void visit(ExpressionStatement expressionStatement) {
    printIndent();
    expressionStatement.getExpression().accept(this);
    print(";");
    println();
  }

  @Override
  public void visit(SwitchStatement switchStatement) {
    printIndent();
    print("switch (");
    switchStatement.getExpression().accept(this);
    print(") {");
    println();
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
    printIndent();
    Expression expression = caseStatement.getExpression();
    if (expression == null) {
      print("default");
    } else {
      print("case ");
      expression.accept(this);
    }
    print(":");
    println();
    incIndent();
    List<Statement> statements = caseStatement.getStatements();
    for (Statement statement : statements) {
      statement.accept(this);
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
