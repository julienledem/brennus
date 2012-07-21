package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 */
public class ThenValueExpressionBuilder<T> extends ValueExpressionBuilder<ThenBuilder<T>, ThenExpressionBuilder<T>, ThenValueExpressionBuilder<T>> {

  ThenValueExpressionBuilder(ExpressionHandler<ThenBuilder<T>> expressionHandler, Expression expression) {
    super(new ThenExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  /**
   * ends the if expression and starts the then block
   * @return
   */
  public ThenBuilder<T> thenBlock() {
    return this.end();
  }

}
