package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class ThenValueExpressionBuilder<T> extends ValueExpressionBuilder<ThenBuilder<T>, ThenExpressionBuilder<T>, ThenValueExpressionBuilder<T>> {

  ThenValueExpressionBuilder(ExpressionHandler<ThenBuilder<T>> expressionHandler, Expression expression) {
    super(new ThenExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  public ThenBuilder<T> thenBlock() {
    return this.end();
  }

}
