package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.CallMethodExpression;
import brennus.model.Expression;

/**
 * Builds a method call
 * @author Julien Le Dem
 *
 * @param <T> type of the parent
 * @param <EB>
 * @param <VEB> type of the expression builders
 */
public class MethodCallBuilder<T, EB, VEB> {

  private final ExpressionBuilderFactory<T, EB, VEB> factory;
  private final Expression callee;
  private final String methodName;
  private final ExpressionHandler<T> expressionHandler;
  private final List<Expression> parameters = new ArrayList<Expression>();

  MethodCallBuilder(ExpressionBuilderFactory<T, EB, VEB> factory, Expression callee, String methodName, ExpressionHandler<T> expressionHandler) {
    super();
    this.factory = factory;
    this.callee = callee;
    this.expressionHandler = expressionHandler;
    this.methodName = methodName;
  }

  /**
   * pass a parameter to the method
   * @return the expressionbuilder to build the parameter value
   */
  ParamExpressionBuilder<T, EB, VEB> param() {
    return new ParamExpressionBuilder<T, EB, VEB>(
        new ExpressionHandler<MethodCallBuilder<T, EB, VEB>>() {
          public MethodCallBuilder<T, EB, VEB> handleExpression(Expression e) {
            parameters.add(e);
            return MethodCallBuilder.this;
          }
    });
  }

  /**
   * no more parameters to pass to this method
   * (mostly used when no parameters at all)
   * @return the parent
   */
  VEB endCall() {
    return factory.newValueExpressionBuilder(expressionHandler, new CallMethodExpression(callee, methodName, parameters));
  }
}
