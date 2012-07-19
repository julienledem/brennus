package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ParamValueExpressionBuilder<T, VEB> extends ValueExpressionBuilder<MethodCallBuilder<T, VEB>, ParamExpressionBuilder<T, VEB>, ParamValueExpressionBuilder<T, VEB>> {

  ParamValueExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler,
      Expression expression) {
    super(expressionHandler, expression);
  }

  @Override
  protected ParamExpressionBuilder<T, VEB> newExpressionBuilder(ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler) {
    return new ParamExpressionBuilder<T, VEB>(expressionHandler);
  }

  @Override
  protected ParamValueExpressionBuilder<T, VEB> newValueExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler, Expression expression) {
    return new ParamValueExpressionBuilder<T, VEB>(expressionHandler, expression);
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
