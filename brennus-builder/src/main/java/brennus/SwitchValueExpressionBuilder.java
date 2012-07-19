package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class SwitchValueExpressionBuilder<T> extends ValueExpressionBuilder<SwitchBuilder<T>, SwitchExpressionBuilder<T>, SwitchValueExpressionBuilder<T>> {

  SwitchValueExpressionBuilder(ExpressionHandler<SwitchBuilder<T>> expressionHandler,
      Expression expression) {
    super(expressionHandler, expression);
  }

  @Override
  protected SwitchExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<SwitchBuilder<T>> expressionHandler) {
    return new SwitchExpressionBuilder<T>(expressionHandler);
  }

  @Override
  protected SwitchValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<SwitchBuilder<T>> expressionHandler, Expression expression) {
    return new SwitchValueExpressionBuilder<T>(expressionHandler, expression);
  }

  public SwitchBuilder<T> endSwitchOn() {
    return super.end();
  }
}
