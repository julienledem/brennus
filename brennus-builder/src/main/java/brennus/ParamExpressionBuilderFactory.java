package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ParamExpressionBuilderFactory<T, VEB>
    implements
    ExpressionBuilderFactory<MethodCallBuilder<T, VEB>, ParamExpressionBuilder<T, VEB>, ParamValueExpressionBuilder<T, VEB>> {

  @Override
  public ParamValueExpressionBuilder<T, VEB> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler,
      Expression expression) {
    return new ParamValueExpressionBuilder<T, VEB>(expressionHandler, expression);
  }

  @Override
  public ParamExpressionBuilder<T, VEB> newExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler) {
    return new ParamExpressionBuilder<T, VEB>(expressionHandler);
  }

}
