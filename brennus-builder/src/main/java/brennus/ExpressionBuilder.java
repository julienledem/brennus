package brennus;

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

  public MethodCallBuilder<T> call(final String methodName) {
    return new MethodCallBuilder<T>(null, methodName, expressionHandler);
  }

  public ValueExpressionBuilder<T> literal(int i) {
    return new ValueExpressionBuilder<T>(expressionHandler, new LiteralExpression(i));
  }

  public ValueExpressionBuilder<T> literal(String string) {
    return new ValueExpressionBuilder<T>(expressionHandler, new LiteralExpression(string));
  }

  public ValueExpressionBuilder<T> literal(boolean b) {
    return new ValueExpressionBuilder<T>(expressionHandler, new LiteralExpression(b));
  }

}
