package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ArrayIndexValueExpressionBuilder<T> extends ValueExpressionBuilder<T, ArrayIndexExpressionBuilder<T>, ArrayIndexValueExpressionBuilder<T>> {

  public ArrayIndexValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new ArrayIndexExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  public T endGetArrayValue() {
    return super.end();
  }

}
