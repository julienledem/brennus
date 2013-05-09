package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 */
public final class ReturnValueExpressionBuilder<T> extends ValueExpressionBuilder<T, ReturnExpressionBuilder<T>, ReturnValueExpressionBuilder<T>> {

  ReturnValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new ReturnExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  /**
   * ends a return statement
   * @return parent
   */
  public T endReturn() {
    return super.end();
  }

}
