package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

final class ThenExpressionBuilderFactory<T>
    implements
    ExpressionBuilderFactory<ThenBuilder<T>, ThenExpressionBuilder<T>, ThenValueExpressionBuilder<T>> {

  @Override
  public ThenValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<ThenBuilder<T>> expressionHandler, Expression expression) {
    return new ThenValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public ThenExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<ThenBuilder<T>> expressionHandler) {
    return new ThenExpressionBuilder<T>(expressionHandler);
  }
}
