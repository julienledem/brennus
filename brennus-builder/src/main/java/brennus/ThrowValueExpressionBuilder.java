package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ThrowValueExpressionBuilder<T> extends
    ValueExpressionBuilder<T, ThrowExpressionBuilder<T>, ThrowValueExpressionBuilder<T>> {

  ThrowValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(expressionHandler, expression);
  }

  @Override
  protected ThrowExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new ThrowExpressionBuilder<T>(expressionHandler);
  }

  @Override
  protected ThrowValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler, Expression expression) {
    return new ThrowValueExpressionBuilder<T>(expressionHandler, expression);
  }

  public T endThrow() {
    return super.end();
  }

}
