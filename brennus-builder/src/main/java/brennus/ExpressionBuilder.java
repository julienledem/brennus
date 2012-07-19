package brennus;

import brennus.model.CallMethodExpression;
import brennus.model.Expression;
import brennus.model.GetExpression;
import brennus.model.LiteralExpression;

/**
 * builds an expression
 * @author Julien Le Dem
 *
 * @param <T> the type of the parent to return on completion
 */
abstract public class ExpressionBuilder<T,VEB> {

  public interface ExpressionHandler<T> {
    T handleExpression(Expression e);
  }

  private final ExpressionHandler<T> expressionHandler;

  ExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    this.expressionHandler = expressionHandler;
  }

  protected abstract VEB newValueExpressionBuilder(ExpressionHandler<T> expressionHandler, Expression expression);

  public VEB get(String name) {
    return newValueExpressionBuilder(expressionHandler, new GetExpression(name));
  }

  public ParamExpressionBuilder<T, VEB> thisCall(final String methodName) {
    return innerCall(methodName).param();
  }

  private MethodCallBuilder<T, VEB> innerCall(final String methodName) {
    return new MethodCallBuilder<T,VEB>(null, methodName, expressionHandler) {
      @Override
      protected VEB newValueExpressionBuilder(
          ExpressionHandler<T> expressionHandler,
          CallMethodExpression callMethodExpression) {
        return ExpressionBuilder.this.newValueExpressionBuilder(expressionHandler, callMethodExpression);
      }

    };
  }

  public VEB thisCallNoParam(final String methodName) {
    return innerCall(methodName).endCall();
  }

  public VEB literal(int i) {
    return newValueExpressionBuilder(expressionHandler, new LiteralExpression(i));
  }

  public VEB literal(String string) {
    return newValueExpressionBuilder(expressionHandler, new LiteralExpression(string));
  }

  public VEB literal(boolean b) {
    return newValueExpressionBuilder(expressionHandler, new LiteralExpression(b));
  }

}
