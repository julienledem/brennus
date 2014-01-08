package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

final class ArrayIndexExpressionBuilderFactory<T>
    implements
    ExpressionBuilderFactory<T, ArrayIndexExpressionBuilder<T>, ArrayIndexValueExpressionBuilder<T>> {

  @Override
  public ArrayIndexValueExpressionBuilder<T> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new ArrayIndexValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public ArrayIndexExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new ArrayIndexExpressionBuilder<T>(expressionHandler);
  }

}
