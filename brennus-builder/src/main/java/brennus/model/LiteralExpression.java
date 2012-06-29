package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class LiteralExpression extends Expression {

  private final int value;

  public LiteralExpression(int value) {
    this.value = value;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "[" + getClass().getSimpleName() + " " + value + "]";
  }
}
