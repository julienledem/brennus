package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class SetValueExpressionBuilder<T> extends
    ValueExpressionBuilder<T, SetExpressionBuilder<T>, SetValueExpressionBuilder<T>> {

  SetValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(expressionHandler, expression);
  }

  @Override
  protected SetExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new SetExpressionBuilder<T>(expressionHandler);
  }

  @Override
  protected SetValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler, Expression expression) {
    return new SetValueExpressionBuilder<T>(expressionHandler, expression);
  }

  public T endSet() {
    return super.end();
  }

}
