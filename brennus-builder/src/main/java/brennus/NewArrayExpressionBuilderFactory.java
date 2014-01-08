package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

final class NewArrayExpressionBuilderFactory<T>
    implements
    ExpressionBuilderFactory<T, NewArrayExpressionBuilder<T>, NewArrayValueExpressionBuilder<T>> {

  @Override
  public NewArrayValueExpressionBuilder<T> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new NewArrayValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public NewArrayExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new NewArrayExpressionBuilder<T>(expressionHandler);
  }

}
