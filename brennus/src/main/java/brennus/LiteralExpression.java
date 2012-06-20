package brennus;

public class LiteralExpression extends Expression {

  private final int value;

  public LiteralExpression(int value) {
    this.value = value;
  }

  @Override
  public void visit(ExpressionVisitor expressionVisitor) {
    expressionVisitor.visit(this);
  }

  public int getValue() {
    return value;
  }

}
