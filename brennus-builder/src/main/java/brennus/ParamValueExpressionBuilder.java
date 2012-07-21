package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 */
public class ParamValueExpressionBuilder<T, EB, VEB> extends ValueExpressionBuilder<MethodCallBuilder<T, EB, VEB>, ParamExpressionBuilder<T, EB, VEB>, ParamValueExpressionBuilder<T, EB, VEB>> {

  ParamValueExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, EB, VEB>> expressionHandler,
      Expression expression) {
    super(new ParamExpressionBuilderFactory<T, EB, VEB>(), expressionHandler, expression);
  }

  /**
   * end the expression co be passed to the current param
   * @return
   */
  private MethodCallBuilder<T, EB, VEB> endParam() {
    return this.end();
  }

  /**
   * end current param and start next
   * @return
   */
  public ParamExpressionBuilder<T, EB, VEB> nextParam() {
    return this.endParam().param();
  }

  /**
   * done passing parameters
   * @return
   */
  public VEB endCall() {
    return this.endParam().endCall();
  }

}
