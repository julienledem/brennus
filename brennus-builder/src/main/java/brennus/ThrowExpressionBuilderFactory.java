package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

class ThrowExpressionBuilderFactory<T>
    implements
    ExpressionBuilderFactory<T, ThrowExpressionBuilder<T>, ThrowValueExpressionBuilder<T>> {

  @Override
  public ThrowValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new ThrowValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public ThrowExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new ThrowExpressionBuilder<T>(expressionHandler);
  }

}
