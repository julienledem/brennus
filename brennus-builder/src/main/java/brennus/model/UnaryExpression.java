package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class UnaryExpression extends Expression {

  private final UnaryOperator operator;
  private final Expression expression;

  public UnaryExpression(UnaryOperator operator, Expression expression) {
    this.operator = operator;
    this.expression = expression;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public UnaryOperator getOperator() {
    return operator;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    return "[" + getOperator().getRepresentation() + " " + expression + "]";
  }

}
