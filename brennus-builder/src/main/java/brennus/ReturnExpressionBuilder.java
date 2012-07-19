package brennus;

import brennus.model.Expression;

public class ReturnExpressionBuilder<T> extends
    ExpressionBuilder<T, ReturnValueExpressionBuilder<T>> {

  ReturnExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<T> expressionHandler) {
    super(expressionHandler);
  }

  @Override
  protected ReturnValueExpressionBuilder<T> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new ReturnValueExpressionBuilder<T>(expressionHandler, expression);
  }

}
