package brennus;

abstract public class Statement {

  private final Expression expression;

  public Statement(Expression expression) {
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }

  abstract public void visit(StatementVisitor statementVisitor);

}
