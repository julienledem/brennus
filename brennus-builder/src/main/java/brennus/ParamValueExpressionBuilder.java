package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ParamValueExpressionBuilder<T, VEB> extends ValueExpressionBuilder<MethodCallBuilder<T, VEB>, ParamExpressionBuilder<T, VEB>, ParamValueExpressionBuilder<T, VEB>> {

  ParamValueExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler,
      Expression expression) {
    super(new ParamExpressionBuilderFactory<T, VEB>(), expressionHandler, expression);
  }

  private MethodCallBuilder<T, VEB> endParam() {
    return this.end();
  }

  public ParamExpressionBuilder<T, VEB> nextParam() {
    return this.endParam().param();
  }

  public VEB endCall() {
    return this.endParam().endCall();
  }

}
