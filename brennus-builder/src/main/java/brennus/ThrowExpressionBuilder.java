package brennus;

import brennus.model.Expression;

public class ThrowExpressionBuilder<T> extends ExpressionBuilder<T, ThrowValueExpressionBuilder<T>> {

  ThrowExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(expressionHandler);
  }

  @Override
  protected ThrowValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new ThrowValueExpressionBuilder<T>(expressionHandler, expression);
  }

}
