package brennus;


public final class ConstructorCallExpressionBuilder extends
  ExpressionBuilder<ConstructorBuilder, ConstructorCallExpressionBuilder, ConstructorCallValueExpressionBuilder> {

  ConstructorCallExpressionBuilder(ExpressionHandler<ConstructorBuilder> expressionHandler) {
    super(new ConstructorCallExpressionBuilderFactory(), expressionHandler);
  }
}
