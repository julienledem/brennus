package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

class ReturnExpressionBuilderFactory<T>
    implements
    ExpressionBuilderFactory<T, ReturnExpressionBuilder<T>, ReturnValueExpressionBuilder<T>> {


  @Override
  public ReturnValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionBuilder.ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new ReturnValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public ReturnExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new ReturnExpressionBuilder<T>(expressionHandler);
  }

}
