package brennus;

import static brennus.model.UnaryOperator.NOT;
import brennus.model.CallMethodExpression;
import brennus.model.Expression;
import brennus.model.GetExpression;
import brennus.model.LiteralExpression;
import brennus.model.UnaryExpression;
import brennus.model.UnaryOperator;

/**
 * builds an expression
 * @author Julien Le Dem
 *
 * @param <T> the type of the parent to return on completion
 */
abstract public class ExpressionBuilder<T, EB, VEB> {

  public interface ExpressionHandler<T> {
    T handleExpression(Expression e);
  }

  private final ExpressionBuilderFactory<T, EB, VEB> factory;
  private final ExpressionHandler<T> expressionHandler;

  ExpressionBuilder(ExpressionBuilderFactory<T, EB, VEB> factory, ExpressionHandler<T> expressionHandler) {
    this.factory = factory;
    this.expressionHandler = expressionHandler;
  }

  private VEB newValueExpressionBuilder(ExpressionHandler<T> expressionHandler, Expression expression) {
    return factory.newValueExpressionBuilder(expressionHandler, expression);
  }

  private EB newExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    return factory.newExpressionBuilder(expressionHandler);
  }

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

  public EB not() {
    return unaryOperator(NOT);
  }

  private EB unaryOperator(final UnaryOperator operator) {
    return newExpressionBuilder(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return expressionHandler.handleExpression(new UnaryExpression(operator, e));
      }
    });
  }
}
