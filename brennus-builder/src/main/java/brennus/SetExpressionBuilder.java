package brennus;


public class SetExpressionBuilder<T> extends
    ExpressionBuilder<T, SetExpressionBuilder<T>, SetValueExpressionBuilder<T>> {

  SetExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(new SetExpressionBuilderFactory<T>(), expressionHandler);
  }

}
