package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ExecValueExpressionBuilder<T> extends ValueExpressionBuilder<T, ExecExpressionBuilder<T>, ExecValueExpressionBuilder<T>> {

  ExecValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(expressionHandler, expression);
  }

  @Override
  protected ExecExpressionBuilder<T> newExpressionBuilder(
      ExpressionHandler<T> expressionHandler) {
    return new ExecExpressionBuilder<T>(expressionHandler);
  }

  @Override
  protected ExecValueExpressionBuilder<T> newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler, Expression expression) {
    return new ExecValueExpressionBuilder<T>(expressionHandler, expression);
  }


  public T endExec() {
    return this.end();
  }
}
