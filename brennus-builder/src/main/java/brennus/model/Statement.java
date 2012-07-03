package brennus.model;

abstract public class Statement {

  private final Expression expression;
  private final int line;

  public Statement(Expression expression, int line) {
    this.expression = expression;
    this.line = line;
  }

  public Expression getExpression() {
    return expression;
  }

  abstract public void accept(StatementVisitor statementVisitor);

  @Override
  public String toString() {
    return "["+getClass().getSimpleName() + " " + expression + "]";
  }

  public int getLine() {
    return line;
  }
}
