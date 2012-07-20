package brennus;


public class ExecExpressionBuilder<T> extends
    ExpressionBuilder<T, ExecExpressionBuilder<T>, ExecValueExpressionBuilder<T>> {

  ExecExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(new ExecExpressionBuilderFactory<T>(), expressionHandler);
  }

}
