package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 */
public final class SwitchValueExpressionBuilder<T> extends ValueExpressionBuilder<SwitchBuilder<T>, SwitchExpressionBuilder<T>, SwitchValueExpressionBuilder<T>> {

  SwitchValueExpressionBuilder(ExpressionHandler<SwitchBuilder<T>> expressionHandler,
      Expression expression) {
    super(new SwitchExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  /**
   * ends the switch expression and starts the switch block
   * @return
   */
  public SwitchBuilder<T> switchBlock() {
    return super.end();
  }

}
