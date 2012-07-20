package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class SwitchValueExpressionBuilder<T> extends ValueExpressionBuilder<SwitchBuilder<T>, SwitchExpressionBuilder<T>, SwitchValueExpressionBuilder<T>> {

  SwitchValueExpressionBuilder(ExpressionHandler<SwitchBuilder<T>> expressionHandler,
      Expression expression) {
    super(new SwitchExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  public SwitchBuilder<T> endSwitchOn() {
    return super.end();
  }
}
