package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class SetExpressionBuilderFactory<T>
    implements
    ExpressionBuilderFactory<T, SetExpressionBuilder<T>, SetValueExpressionBuilder<T>> {

  @Override
  public SetValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new SetValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public SetExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new SetExpressionBuilder<T>(expressionHandler);
  }

}
