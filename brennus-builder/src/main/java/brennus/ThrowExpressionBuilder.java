package brennus;


public final class ThrowExpressionBuilder<T> extends ExpressionBuilder<T, ThrowExpressionBuilder<T>, ThrowValueExpressionBuilder<T>> {

  ThrowExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(new ThrowExpressionBuilderFactory<T>(), expressionHandler);
  }

}
