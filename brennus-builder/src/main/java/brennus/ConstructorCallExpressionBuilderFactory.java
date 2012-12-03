package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ConstructorCallExpressionBuilderFactory
    implements
    ExpressionBuilderFactory<ConstructorBuilder, ConstructorCallExpressionBuilder, ConstructorCallValueExpressionBuilder> {

  @Override
  public ConstructorCallValueExpressionBuilder newValueExpressionBuilder(
      ExpressionHandler<ConstructorBuilder> expressionHandler,
      Expression expression) {
    return new ConstructorCallValueExpressionBuilder(expressionHandler, expression);
  }

  @Override
  public ConstructorCallExpressionBuilder newExpressionBuilder(
      ExpressionHandler<ConstructorBuilder> expressionHandler) {
    return new ConstructorCallExpressionBuilder(expressionHandler);
  }
}
