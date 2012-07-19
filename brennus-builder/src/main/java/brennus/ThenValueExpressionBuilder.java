package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ThenValueExpressionBuilder<T> extends ValueExpressionBuilder<ThenBuilder<T>, ThenExpressionBuilder<T>, ThenValueExpressionBuilder<T>> {

  ThenValueExpressionBuilder(ExpressionHandler<ThenBuilder<T>> expressionHandler, Expression expression) {
    super(expressionHandler, expression);
  }

  public ThenBuilder<T> thenBlock() {
    return this.end();
  }

  @Override
  protected ThenExpressionBuilder<T> newExpressionBuilder(ExpressionHandler<ThenBuilder<T>> expressionHandler) {
    return new ThenExpressionBuilder<T>(expressionHandler);
  }

  @Override
  protected ThenValueExpressionBuilder<T> newValueExpressionBuilder(ExpressionHandler<ThenBuilder<T>> expressionHandler, Expression expression) {
    return new ThenValueExpressionBuilder<T>(expressionHandler, expression);
  }
}
