package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class SwitchExpressionBuilderFactory<T>
    implements
    ExpressionBuilderFactory<SwitchBuilder<T>, SwitchExpressionBuilder<T>, SwitchValueExpressionBuilder<T>> {

  @Override
  public SwitchValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<SwitchBuilder<T>> expressionHandler,
      Expression expression) {
    return new SwitchValueExpressionBuilder<T>(expressionHandler, expression);
  }

  @Override
  public SwitchExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<SwitchBuilder<T>> expressionHandler) {
    return new SwitchExpressionBuilder<T>(expressionHandler);
  }


}
