package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ThrowValueExpressionBuilder<T> extends
    ValueExpressionBuilder<T, ThrowExpressionBuilder<T>, ThrowValueExpressionBuilder<T>> {

  ThrowValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new ThrowExpressionBuilderFactory<T>(),expressionHandler, expression);
  }

  public T endThrow() {
    return super.end();
  }

}
