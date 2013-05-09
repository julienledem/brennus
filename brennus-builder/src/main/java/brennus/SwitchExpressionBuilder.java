package brennus;


public final class SwitchExpressionBuilder<T> extends
    ExpressionBuilder<SwitchBuilder<T>, SwitchExpressionBuilder<T>, SwitchValueExpressionBuilder<T>> {

  SwitchExpressionBuilder(
      ExpressionHandler<SwitchBuilder<T>> expressionHandler) {
    super(new SwitchExpressionBuilderFactory<T>(), expressionHandler);
  }

}
