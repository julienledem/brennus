package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class AddExpression extends Expression {

  private final Expression leftExpression;
  private final Expression rightExpression;

  public AddExpression(Expression leftExpression, Expression rightExpression) {
    this.leftExpression = leftExpression;
    this.rightExpression = rightExpression;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public Expression getLeftExpression() {
    return leftExpression;
  }

  public Expression getRightExpression() {
    return rightExpression;
  }

  @Override
  public String toString() {
    return "["+leftExpression + " + " + rightExpression+"]";
  }
}
