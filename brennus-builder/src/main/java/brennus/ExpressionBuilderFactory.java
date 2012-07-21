package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.model.Expression;

interface ExpressionBuilderFactory<T, EB, EVB> {

  EVB newValueExpressionBuilder(
      ExpressionHandler<T> expressionHandler,
      Expression expression);

  EB newExpressionBuilder(
      ExpressionHandler<T> expressionHandler);

}
