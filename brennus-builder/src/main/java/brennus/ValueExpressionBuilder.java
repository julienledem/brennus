package brennus;

import static brennus.model.BinaryOperator.AND;
import static brennus.model.BinaryOperator.EQUALS;
import static brennus.model.BinaryOperator.GETARRAYATINDEX;
import static brennus.model.BinaryOperator.GREATER_THAN;
import static brennus.model.BinaryOperator.PLUS;
import static brennus.model.UnaryOperator.ARRAYSIZE;
import static brennus.model.UnaryOperator.ISNOTNULL;
import static brennus.model.UnaryOperator.ISNULL;
import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.BinaryExpression;
import brennus.model.BinaryOperator;
import brennus.model.CastExpression;
import brennus.model.ExistingType;
import brennus.model.Expression;
import brennus.model.InstanceOfExpression;
import brennus.model.NewArrayExpression;
import brennus.model.Type;
import brennus.model.UnaryExpression;

/**
 * builder for operations that can be applied to a value
 *
 * @author Julien Le Dem
 *
 * @param <T> the Type of the parent
 * @param <EB> the type of the next ExpressionBuilder
 * @param <VEB> the type of the next ValueExpressionBuilder
 */
abstract public class ValueExpressionBuilder<T, EB, VEB> {

  private final ExpressionBuilderFactory<T, EB, VEB> factory;
  private final ExpressionHandler<T> expressionHandler;
  private final Expression expression;

  /**
   * @param factory to get expressionBuilders of the right type
   * @param expressionHandler the handler that will receive the resulting expression
   * @param expression the parent expression (left operand)
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

  public VEB isNull() {
    return newValueExpressionBuilder(expressionHandler, new UnaryExpression(ISNULL, expression));
  }

  public VEB isNotNull() {
    return newValueExpressionBuilder(expressionHandler, new UnaryExpression(ISNOTNULL, expression));
  }

  public VEB arraySize() {
    return newValueExpressionBuilder(expressionHandler, new UnaryExpression(ARRAYSIZE, expression));
  }

  public VEB instanceOf(ExistingType existingType) {
    return newValueExpressionBuilder(expressionHandler, new InstanceOfExpression(expression, existingType));
  }

  public VEB castTo(Type type) {
    return newValueExpressionBuilder(expressionHandler, new CastExpression(type, expression));
  }

  T end() {
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

  public ParamExpressionBuilder<T, EB, VEB> call(String methodName) {
    return new MethodCallBuilder<T, EB, VEB>(factory, expression, methodName, expressionHandler).param();
  }

  public VEB callNoParam(String methodName) {
    return new MethodCallBuilder<T, EB, VEB>(factory, expression, methodName, expressionHandler).endCall();
  }


  public ArrayIndexExpressionBuilder<VEB> getArrayValueAt() {
    return new ArrayIndexExpressionBuilder<VEB>(new ExpressionHandler<VEB>() {
          public VEB handleExpression(Expression e) {
            return factory.newValueExpressionBuilder(expressionHandler, new BinaryExpression(expression, GETARRAYATINDEX, e));
          }
    });
  }

}
