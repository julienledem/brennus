package brennus;

import static brennus.model.UnaryOperator.NOT;
import brennus.model.Expression;
import brennus.model.GetExpression;
import brennus.model.InstantiationExpression;
import brennus.model.LiteralExpression;
import brennus.model.Type;
import brennus.model.UnaryExpression;
import brennus.model.UnaryOperator;

/**
 * builds an expression
 * @author Julien Le Dem
 *
 * @param <T> the type of the parent to return on completion
 * @param <EB> the actual type of this expression builder
 * @param <VEB> the type returned when starting expression values
 */
abstract public class ExpressionBuilder<T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> {

  interface ExpressionHandler<T> {
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

  private MethodCallBuilder<T, EB, VEB> innerCall(final String methodName) {
    return new MethodCallBuilder<T, EB, VEB>(factory, null, methodName, expressionHandler);
  }

  private EB unaryOperator(final UnaryOperator operator) {
    return newExpressionBuilder(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return expressionHandler.handleExpression(new UnaryExpression(operator, e));
      }
    });
  }

  public VEB get(String name) {
    return newValueExpressionBuilder(expressionHandler, new GetExpression(name));
  }

  public ParamExpressionBuilder<T, EB, VEB> callOnThis(final String methodName) {
    return innerCall(methodName).param();
  }

  public VEB callOnThisNoParam(final String methodName) {
    return innerCall(methodName).endCall();
  }

  public VEB literal(int i) {
    return newValueExpressionBuilder(expressionHandler, new LiteralExpression(i));
  }

  public VEB literal(long l) {
    return newValueExpressionBuilder(expressionHandler, new LiteralExpression(l));
  }

  public VEB literal(float f) {
    return newValueExpressionBuilder(expressionHandler, new LiteralExpression(f));
  }

  public VEB literal(double d) {
    return newValueExpressionBuilder(expressionHandler, new LiteralExpression(d));
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

  @SuppressWarnings("unchecked")
  public <R> R map(Function<EB, R> function) {
    // TODO: figure if the generic type can be defined correctly
    return function.apply((EB)this);
  }

  public VEB newInstanceNoParam(Type type) {
    return newValueExpressionBuilder(expressionHandler, new InstantiationExpression(type, ImmutableList.<Expression>empty()));
  }
}
