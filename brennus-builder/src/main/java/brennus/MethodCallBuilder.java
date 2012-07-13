package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.CallMethodExpression;
import brennus.model.Expression;

public class MethodCallBuilder<T>/* extends StatementBuilder<T> */ {

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
  public ExpressionBuilder<MethodCallBuilder<T>> param() {
    return new ExpressionBuilder<MethodCallBuilder<T>>(
        new ExpressionHandler<MethodCallBuilder<T>>() {
          public MethodCallBuilder<T> handleExpression(Expression e) {
            parameters.add(e);
            return MethodCallBuilder.this;
          }
    });
  }

//  /**
//   * @return the handler for statements created in this method
//   */
//  @Override
//  protected StatementHandler<T> statementHandler() {
//    // in this case we are saying we are done with parameters.
//    // so the handler will finalize this method and add the next statement to the parent
//    // TODO: fix type declaration
//    return ((StatementBuilder<T>)expressionHandler.handleExpression(new CallMethodExpression(callee, methodName, parameters))).statementHandler();
//  }

  /**
   * no more parameters to pass to this method
   * (mostly used when no parameters at all)
   * @return the parent
   */
  public ValueExpressionBuilder<T> endCall() {
    return new ValueExpressionBuilder<T>(expressionHandler, new CallMethodExpression(callee, methodName, parameters));
  }

}
