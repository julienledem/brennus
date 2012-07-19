package brennus;

import brennus.model.Expression;

public final class ThenExpressionBuilder<T> extends ExpressionBuilder<ThenBuilder<T>, ThenValueExpressionBuilder<T>> {

  ThenExpressionBuilder( ExpressionHandler<ThenBuilder<T>> expressionHandler) {
    super(expressionHandler);
  }

  @Override
  protected ThenValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<ThenBuilder<T>> expressionHandler, Expression expression) {
    return new ThenValueExpressionBuilder<T>(expressionHandler, expression);
  }
}