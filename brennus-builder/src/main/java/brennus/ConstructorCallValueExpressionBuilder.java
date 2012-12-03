package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ConstructorCallValueExpressionBuilder extends
    ValueExpressionBuilder<ConstructorBuilder, ConstructorCallExpressionBuilder, ConstructorCallValueExpressionBuilder> {

  ConstructorCallValueExpressionBuilder(ExpressionHandler<ConstructorBuilder> expressionHandler,
      Expression expression) {
    super(new ConstructorCallExpressionBuilderFactory(), expressionHandler, expression);
  }

  /**
   * end an exec statement
   * @return parent
   */
  public ConstructorBuilder endConstructorCall() {
    return this.end();
  }
}
