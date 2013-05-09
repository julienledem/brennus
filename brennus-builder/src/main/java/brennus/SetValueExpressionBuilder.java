package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 */
public final class SetValueExpressionBuilder<T> extends
    ValueExpressionBuilder<T, SetExpressionBuilder<T>, SetValueExpressionBuilder<T>> {

  SetValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new SetExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  /**
   * ends the set statement
   * @return parent
   */
  public T endSet() {
    return super.end();
  }

}
