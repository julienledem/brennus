package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.CallConstructorExpression;
import brennus.model.Expression;

/**
 * Builds a constructor call
 * @author Julien Le Dem
 *
 */
public final class ConstructorCallBuilder {

  private final ExpressionBuilderFactory<ConstructorBuilder, ConstructorCallExpressionBuilder, ConstructorCallValueExpressionBuilder> factory;
  private final ExpressionHandler<ConstructorBuilder> expressionHandler;
  private final ImmutableList<Expression> parameters;

  ConstructorCallBuilder(ConstructorCallExpressionBuilderFactory factory, ExpressionHandler<ConstructorBuilder> expressionHandler) {
    this(
        factory,
        expressionHandler,
        ImmutableList.<Expression>empty());
  }

  private ConstructorCallBuilder(
      ExpressionBuilderFactory<ConstructorBuilder, ConstructorCallExpressionBuilder, ConstructorCallValueExpressionBuilder> factory,
      ExpressionHandler<ConstructorBuilder> expressionHandler,
      ImmutableList<Expression> parameters) {
    super();
    this.factory = factory;
    this.expressionHandler = expressionHandler;
    this.parameters = parameters;
  }

  /**
   * pass a parameter to the constructor
   * @return the expressionbuilder to build the parameter value
   */
  ConstructorParamExpressionBuilder param() {
    return new ConstructorParamExpressionBuilder(
        new ExpressionHandler<ConstructorCallBuilder>() {
          public ConstructorCallBuilder handleExpression(Expression e) {
            return new ConstructorCallBuilder(
                factory,
                expressionHandler,
                parameters.append(e));
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
