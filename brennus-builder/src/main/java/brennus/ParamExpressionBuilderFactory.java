package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

final class ParamExpressionBuilderFactory<T, EB, VEB>
    implements
    ExpressionBuilderFactory<MethodCallBuilder<T, EB, VEB>, ParamExpressionBuilder<T, EB, VEB>, ParamValueExpressionBuilder<T, EB, VEB>> {

  @Override
  public ParamValueExpressionBuilder<T, EB, VEB> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<MethodCallBuilder<T, EB, VEB>> expressionHandler,
      Expression expression) {
    return new ParamValueExpressionBuilder<T, EB, VEB>(expressionHandler, expression);
  }

  @Override
  public ParamExpressionBuilder<T, EB, VEB> newExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, EB, VEB>> expressionHandler) {
    return new ParamExpressionBuilder<T, EB, VEB>(expressionHandler);
  }

}
