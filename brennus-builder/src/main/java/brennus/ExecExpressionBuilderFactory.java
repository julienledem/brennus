package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ExecExpressionBuilderFactory<T> implements
    ExpressionBuilderFactory<T, ExecExpressionBuilder<T>, ExecValueExpressionBuilder<T>> {

  @Override
  public ExecValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new ExecValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public ExecExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new ExecExpressionBuilder<T>(expressionHandler);
  }
}
