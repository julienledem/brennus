package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.AddExpression;
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
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return expressionHandler.handleExpression(new AddExpression(expression, e));
      }
    });
  }

  public T end() {
    return expressionHandler.handleExpression(expression);
  }

}
