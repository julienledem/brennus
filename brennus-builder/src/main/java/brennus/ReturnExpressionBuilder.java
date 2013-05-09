package brennus;


public final class ReturnExpressionBuilder<T> extends
    ExpressionBuilder<T, ReturnExpressionBuilder<T>, ReturnValueExpressionBuilder<T>> {

  ReturnExpressionBuilder(
      brennus.ExpressionBuilder.ExpressionHandler<T> expressionHandler) {
    super(new ReturnExpressionBuilderFactory<T>(), expressionHandler);
  }

}
