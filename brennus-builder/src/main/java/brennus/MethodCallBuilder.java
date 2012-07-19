package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.CallMethodExpression;
import brennus.model.Expression;

abstract public class MethodCallBuilder<T, VEB> {

  private final Expression callee;
  private final String methodName;
  private final ExpressionHandler<T> expressionHandler;
  private final List<Expression> parameters = new ArrayList<Expression>();

  public MethodCallBuilder(Expression callee, String methodName, ExpressionHandler<T> expressionHandler) {
    super();
    this.callee = callee;
    this.expressionHandler = expressionHandler;
    this.methodName = methodName;
  }

  /**
   * pass a parameter to the method
   * @return the expressionbuilder to build the parameter value
   */
  public ParamExpressionBuilder<T, VEB> param() {
    return new ParamExpressionBuilder<T, VEB>(
        new ExpressionHandler<MethodCallBuilder<T, VEB>>() {
          public MethodCallBuilder<T, VEB> handleExpression(Expression e) {
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
  public VEB endCall() {
    return newValueExpressionBuilder(expressionHandler, new CallMethodExpression(callee, methodName, parameters));
  }

  abstract protected VEB newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      CallMethodExpression callMethodExpression);

}
