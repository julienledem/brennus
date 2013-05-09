package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

/**
 *
 * @author Julien Le Dem
 *
 */
public final class ConstructorParamValueExpressionBuilder extends ValueExpressionBuilder<ConstructorCallBuilder, ConstructorParamExpressionBuilder, ConstructorParamValueExpressionBuilder> {

  ConstructorParamValueExpressionBuilder(
      ExpressionHandler<ConstructorCallBuilder> expressionHandler,
      Expression expression) {
    super(new ConstructorParamExpressionBuilderFactory(), expressionHandler, expression);
  }

  /**
   * end the expression to be passed to the current param
   * @return
   */
  private ConstructorCallBuilder endParam() {
    return this.end();
  }

  /**
   * shortcut: endParam().nextParam()
   * end current param and start next
   * @return a builder for the next param expression
   */
  public ConstructorParamExpressionBuilder nextParam() {
    return this.endParam().param();
  }

  /**
   * shortcut: endParam().endConstructorCall()
   * done passing parameters
   * @return a builder for the Constructor body
   */
  public ConstructorBuilder endConstructorCall() {
    return this.endParam().endConstructorCall();
  }

}
