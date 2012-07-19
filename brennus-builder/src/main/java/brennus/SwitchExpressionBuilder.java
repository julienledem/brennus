package brennus;

import brennus.model.Expression;

public class SwitchExpressionBuilder<T> extends
    ExpressionBuilder<SwitchBuilder<T>, SwitchValueExpressionBuilder<T>> {

  SwitchExpressionBuilder(
      ExpressionHandler<SwitchBuilder<T>> expressionHandler) {
    super(expressionHandler);
  }

  @Override
  protected SwitchValueExpressionBuilder<T> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<SwitchBuilder<T>> expressionHandler,
      Expression expression) {
    return new SwitchValueExpressionBuilder<T>(expressionHandler, expression);
  }

}
