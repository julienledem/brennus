package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.CallConstructorExpression;
import brennus.model.Expression;

/**
 * Builds a constructor call
 * @author Julien Le Dem
 *
 */
public class ConstructorCallBuilder {

  private final ExpressionBuilderFactory<ConstructorBuilder, ConstructorCallExpressionBuilder, ConstructorCallValueExpressionBuilder> factory;
  private final ExpressionHandler<ConstructorBuilder> expressionHandler;
  private final List<Expression> parameters = new ArrayList<Expression>();

  ConstructorCallBuilder(ConstructorCallExpressionBuilderFactory factory, ExpressionHandler<ConstructorBuilder> expressionHandler) {
    super();
    this.factory = factory;
    this.expressionHandler = expressionHandler;
  }

  /**
   * pass a parameter to the constructor
   * @return the expressionbuilder to build the parameter value
   */
  ConstructorParamExpressionBuilder param() {
    return new ConstructorParamExpressionBuilder(
        new ExpressionHandler<ConstructorCallBuilder>() {
          public ConstructorCallBuilder handleExpression(Expression e) {
            parameters.add(e);
            return ConstructorCallBuilder.this;
          }
    });
  }

  /**
   * no more parameters to pass to this constructor
   * (mostly used when no parameters at all)
   * @return the ConstructoBuilder
   */
  ConstructorBuilder endConstructorCall() {
    return factory
        // pass the expression for the last param
        .newValueExpressionBuilder(expressionHandler, new CallConstructorExpression(parameters))
        // and go directly from expression to statement as a constructor never returns anything
        .end();
  }
}
