package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

class ConstructorParamExpressionBuilderFactory
    implements
    ExpressionBuilderFactory<ConstructorCallBuilder, ConstructorParamExpressionBuilder, ConstructorParamValueExpressionBuilder> {

  @Override
  public ConstructorParamValueExpressionBuilder newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<ConstructorCallBuilder> expressionHandler,
      Expression expression) {
    return new ConstructorParamValueExpressionBuilder(expressionHandler, expression);
  }

  @Override
  public ConstructorParamExpressionBuilder newExpressionBuilder(
      ExpressionHandler<ConstructorCallBuilder> expressionHandler) {
    return new ConstructorParamExpressionBuilder(expressionHandler);
  }

}
