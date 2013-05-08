package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class LiteralExpression extends Expression {

  private final Object value;
  private final ExistingType type;

  public LiteralExpression(int value) {
    this.value = value;
    this.type = ExistingType.INT;
  }

  public LiteralExpression(String value) {
    this.value = value;
    this.type = ExistingType.STRING;
  }

  public LiteralExpression(boolean value) {
    this.value = value;
    this.type = ExistingType.BOOLEAN;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "[" + getClass().getSimpleName() + " " + value + "]";
  }

  public ExistingType getType() {
    return type;
  }
}
