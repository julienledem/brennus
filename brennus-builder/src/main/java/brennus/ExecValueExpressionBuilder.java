package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 */
public final class ExecValueExpressionBuilder<T> extends ValueExpressionBuilder<T, ExecExpressionBuilder<T>, ExecValueExpressionBuilder<T>> {

  ExecValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new ExecExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  /**
   * end an exec statement
   * @return parent
   */
  public T endExec() {
    return this.end();
  }

}
