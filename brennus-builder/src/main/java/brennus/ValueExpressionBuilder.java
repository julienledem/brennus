package brennus;

import static brennus.model.BinaryOperator.AND;
import static brennus.model.BinaryOperator.EQUALS;
import static brennus.model.BinaryOperator.GREATER_THAN;
import static brennus.model.BinaryOperator.PLUS;
import static brennus.model.UnaryOperator.NOT;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.BinaryExpression;
import brennus.model.BinaryOperator;
import brennus.model.CastExpression;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.InstanceOfExpression;
import brennus.model.Type;
import brennus.model.UnaryExpression;
import brennus.model.UnaryOperator;

public class ValueExpressionBuilder<T> {

  private final ExpressionHandler<T> expressionHandler;
  private final Expression expression;

  /**
   *
   * @param expressionHandler the handler that will receive the resulting expression
   * @param expression the paarent expression (left operand)
   */
  ValueExpressionBuilder(ExpressionHandler<T> expressionHandler, Expression expression) {
        this.expressionHandler = expressionHandler;
        this.expression = expression;
  }

  public ValueExpressionBuilder<T> not() {
    return unaryOperator(NOT);
  }

  public ExpressionBuilder<T> and() {
    return binaryOperator(AND);
  }

  public ExpressionBuilder<T> add() {
    return binaryOperator(PLUS);
  }

  public ExpressionBuilder<T> isEqualTo() {
    return binaryOperator(EQUALS);
  }

  public ExpressionBuilder<T> isGreaterThan() {
    return binaryOperator(GREATER_THAN);
  }

  public ValueExpressionBuilder<T> instanceOf(ExistingType existingType) {
    return new ValueExpressionBuilder<T>(expressionHandler, new InstanceOfExpression(expression, existingType));
  }

  public T end() {
    return expressionHandler.handleExpression(expression);
  }

  private ExpressionBuilder<T> binaryOperator(final BinaryOperator operator) {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return expressionHandler.handleExpression(new BinaryExpression(expression, operator, e));
      }
    });
  }

  private ValueExpressionBuilder<T> unaryOperator(UnaryOperator operator) {
    return new ValueExpressionBuilder<T>(expressionHandler, new UnaryExpression(operator, expression));
  }

  public MethodCallBuilder<T> call(String methodName) {
    return new MethodCallBuilder<T>(expression, methodName, expressionHandler);
  }

  public ValueExpressionBuilder<T> castTo(Type type) {
    return new ValueExpressionBuilder<T>(expressionHandler, new CastExpression(type, expression));
  }
}
