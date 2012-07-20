package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ReturnValueExpressionBuilder<T> extends ValueExpressionBuilder<T, ReturnExpressionBuilder<T>, ReturnValueExpressionBuilder<T>> {

  ReturnValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new ReturnExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  public T endReturn() {
    return super.end();
  }

}
