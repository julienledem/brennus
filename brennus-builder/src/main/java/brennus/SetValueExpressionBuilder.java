package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

public class SetValueExpressionBuilder<T> extends
    ValueExpressionBuilder<T, SetExpressionBuilder<T>, SetValueExpressionBuilder<T>> {

  SetValueExpressionBuilder(ExpressionHandler<T> expressionHandler,
      Expression expression) {
    super(new SetExpressionBuilderFactory<T>(), expressionHandler, expression);
  }

  public T endSet() {
    return super.end();
  }

}
