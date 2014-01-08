package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class NewArrayValueExpressionBuilder<T> extends ValueExpressionBuilder<T, NewArrayExpressionBuilder<T>, NewArrayValueExpressionBuilder<T>> {

  public NewArrayValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new NewArrayExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  public T endNewArray() {
    return super.end();
  }

}
