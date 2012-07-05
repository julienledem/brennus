package brennus;

import static brennus.model.BinaryOperator.*;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.BinaryExpression;
import brennus.model.BinaryOperator;
import brennus.model.Expression;

public class ValueExpressionBuilder<T> {

  private final ExpressionHandler<T> expressionHandler;
  private final Expression expression;

  /**
   *
   * @param expressionHandler the handler that will receive the resulting expression
   * @param expression the paarent expression (left operand)
   */
  ValueExpressionBuilder(ExpressionHandler<T> expressionHandler, Expression expression) {
        this.expressionHandler = expressionHandler;
        this.expression = expression;
  }

  public ExpressionBuilder<T> add() {
    return operator(PLUS);
  }

  public ExpressionBuilder<T> isEqualTo() {
    return operator(EQUALS);
  }

  public ExpressionBuilder<T> isGreaterThan() {
    return operator(GREATER_THAN);
  }

  public T end() {
    return expressionHandler.handleExpression(expression);
  }

  private ExpressionBuilder<T> operator(final BinaryOperator operator) {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return expressionHandler.handleExpression(new BinaryExpression(expression, operator, e));
      }
    });
  }

}
