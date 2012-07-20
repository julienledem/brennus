package brennus;

import static brennus.model.BinaryOperator.AND;
import static brennus.model.BinaryOperator.EQUALS;
import static brennus.model.BinaryOperator.GREATER_THAN;
import static brennus.model.BinaryOperator.PLUS;
import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.BinaryExpression;
import brennus.model.BinaryOperator;
import brennus.model.CallMethodExpression;
import brennus.model.CastExpression;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.InstanceOfExpression;
import brennus.model.Type;

abstract public class ValueExpressionBuilder<T, EB, VEB> {

  private final ExpressionBuilderFactory<T, EB, VEB> factory;
  private final ExpressionHandler<T> expressionHandler;
  private final Expression expression;

  /**
   *
   * @param expressionHandler the handler that will receive the resulting expression
   * @param expression the paarent expression (left operand)
   */
  ValueExpressionBuilder(ExpressionBuilderFactory<T, EB, VEB> factory, ExpressionHandler<T> expressionHandler, Expression expression) {
        this.factory = factory;
        this.expressionHandler = expressionHandler;
        this.expression = expression;
  }

  public EB and() {
    return binaryOperator(AND);
  }

  public EB add() {
    return binaryOperator(PLUS);
  }

  public EB isEqualTo() {
    return binaryOperator(EQUALS);
  }

  public EB isGreaterThan() {
    return binaryOperator(GREATER_THAN);
  }

  public VEB instanceOf(ExistingType existingType) {
    return newValueExpressionBuilder(expressionHandler, new InstanceOfExpression(expression, existingType));
  }

  public VEB castTo(Type type) {
    return newValueExpressionBuilder(expressionHandler, new CastExpression(type, expression));
  }

  protected T end() {
    return expressionHandler.handleExpression(expression);
  }

  private EB newExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    return factory.newExpressionBuilder(expressionHandler);
  }

  private VEB newValueExpressionBuilder(ExpressionHandler<T> expressionHandler, Expression expression) {
    return factory.newValueExpressionBuilder(expressionHandler, expression);
  }

  private EB binaryOperator(final BinaryOperator operator) {
    return newExpressionBuilder(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return expressionHandler.handleExpression(new BinaryExpression(expression, operator, e));
      }
    });
  }

  public ParamExpressionBuilder<T, VEB> call(String methodName) {
    return new MethodCallBuilder<T, VEB>(expression, methodName, expressionHandler) {
      @Override
      protected VEB newValueExpressionBuilder(
          ExpressionHandler<T> expressionHandler,
          CallMethodExpression callMethodExpression) {
        return ValueExpressionBuilder.this.newValueExpressionBuilder(expressionHandler, callMethodExpression);
      }
    }.param();
  }

}
