package brennus;

import brennus.model.Expression;

public class ExecExpressionBuilder<T> extends
    ExpressionBuilder<T, ExecValueExpressionBuilder<T>> {

  ExecExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(expressionHandler);
  }

  @Override
  protected ExecValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression) {
    return new ExecValueExpressionBuilder<T>(expressionHandler, expression);
  }

}
