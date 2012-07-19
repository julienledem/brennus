package brennus;

import brennus.model.Expression;

public class SetExpressionBuilder<T> extends
    ExpressionBuilder<T, SetValueExpressionBuilder<T>> {

  SetExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(expressionHandler);
  }

  @Override
  protected SetValueExpressionBuilder<T> newValueExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new SetValueExpressionBuilder<T>(expressionHandler, expression);
  }

}
