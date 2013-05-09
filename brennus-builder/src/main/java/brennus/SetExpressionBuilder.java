package brennus;


public final class SetExpressionBuilder<T> extends
    ExpressionBuilder<T, SetExpressionBuilder<T>, SetValueExpressionBuilder<T>> {

  SetExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(new SetExpressionBuilderFactory<T>(), expressionHandler);
  }

}
