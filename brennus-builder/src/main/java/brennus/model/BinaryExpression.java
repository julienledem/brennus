package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class BinaryExpression extends Expression {

  private final Expression leftExpression;
  private final BinaryOperator operator;
  private final Expression rightExpression;

  public BinaryExpression(Expression leftExpression, BinaryOperator operator, Expression rightExpression) {
    this.leftExpression = leftExpression;
    this.operator = operator;
    this.rightExpression = rightExpression;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public Expression getLeftExpression() {
    return leftExpression;
  }

  public BinaryOperator getOperator() {
    return operator;
  }

  public Expression getRightExpression() {
    return rightExpression;
  }

  @Override
  public String toString() {
    return "["+leftExpression + " " + getOperator().getRepresentation() + " " + rightExpression+"]";
  }
}
