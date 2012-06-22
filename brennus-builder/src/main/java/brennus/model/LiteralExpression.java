package brennus.model;

public class LiteralExpression extends Expression {

  private final int value;

  public LiteralExpression(int value) {
    this.value = value;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    expressionVisitor.visit(this);
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "[" + getClass().getSimpleName() + " " + value + "]";
  }
}
