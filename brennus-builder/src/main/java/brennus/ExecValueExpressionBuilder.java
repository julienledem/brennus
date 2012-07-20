package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ExecValueExpressionBuilder<T> extends ValueExpressionBuilder<T, ExecExpressionBuilder<T>, ExecValueExpressionBuilder<T>> {

  ExecValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new ExecExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  public T endExec() {
    return this.end();
  }
}
