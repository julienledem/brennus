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
public class ExpressionBuilder<T> {

  public interface ExpressionHandler<T> {
    T handleExpression(Expression e);
  }

  private final ExpressionHandler<T> expressionHandler;

  ExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    this.expressionHandler = expressionHandler;
  }

  public ValueExpressionBuilder<T> get(String name) {
    return new ValueExpressionBuilder<T>(expressionHandler, new GetExpression(name));
  }

  public ValueExpressionBuilder<T> call(String methodName) {
    return new ValueExpressionBuilder<T>(expressionHandler, new CallMethodExpression(methodName));
  }

  public ValueExpressionBuilder<T> literal(int i) {
    return new ValueExpressionBuilder<T>(expressionHandler, new LiteralExpression(i));
  }

}
