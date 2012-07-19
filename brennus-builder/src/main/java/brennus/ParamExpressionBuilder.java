package brennus;

import brennus.model.Expression;

public class ParamExpressionBuilder<T, VEB> extends ExpressionBuilder<MethodCallBuilder<T, VEB>, ParamValueExpressionBuilder<T, VEB>> {

  ParamExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler) {
    super(expressionHandler);
  }

  @Override
  protected ParamValueExpressionBuilder<T, VEB> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler,
      Expression expression) {
    return new ParamValueExpressionBuilder<T, VEB>(expressionHandler, expression);
  }

}
