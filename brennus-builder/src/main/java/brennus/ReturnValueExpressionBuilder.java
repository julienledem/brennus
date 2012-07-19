package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ReturnValueExpressionBuilder<T> extends ValueExpressionBuilder<T, ReturnExpressionBuilder<T>, ReturnValueExpressionBuilder<T>> {

  ReturnValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(expressionHandler, expression);
  }

  @Override
  protected ReturnExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new ReturnExpressionBuilder<T>(expressionHandler);
  }

  @Override
  protected ReturnValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler, Expression expression) {
    return new ReturnValueExpressionBuilder<T>(expressionHandler, expression);
  }

  public T endReturn() {
    return super.end();
  }

}
