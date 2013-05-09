package brennus;


public final class ConstructorParamExpressionBuilder extends ExpressionBuilder<ConstructorCallBuilder, ConstructorParamExpressionBuilder, ConstructorParamValueExpressionBuilder> {

  ConstructorParamExpressionBuilder(
      ExpressionHandler<ConstructorCallBuilder> expressionHandler) {
    super(new ConstructorParamExpressionBuilderFactory(), expressionHandler);
  }

}
