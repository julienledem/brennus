package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 */
public class ThrowValueExpressionBuilder<T> extends
    ValueExpressionBuilder<T, ThrowExpressionBuilder<T>, ThrowValueExpressionBuilder<T>> {

  ThrowValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new ThrowExpressionBuilderFactory<T>(),expressionHandler, expression);
  }

  /**
   * ends a throw statement
   * @return parent
   */
  public T endThrow() {
    return super.end();
  }

}
