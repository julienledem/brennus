package brennus;


public final class ThenExpressionBuilder<T> extends ExpressionBuilder<ThenBuilder<T>, ThenExpressionBuilder<T>, ThenValueExpressionBuilder<T>> {

  ThenExpressionBuilder( ExpressionHandler<ThenBuilder<T>> expressionHandler) {
    super(new ThenExpressionBuilderFactory<T>(), expressionHandler);
  }

}