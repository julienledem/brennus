package brennus.model;

abstract public class Statement {

  private final Expression expression;

  public Statement(Expression expression) {
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }

  abstract public void accept(StatementVisitor statementVisitor);

  @Override
  public String toString() {
    return "["+getClass().getSimpleName() + " " + expression + "]";
  }
}
