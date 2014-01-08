package brennus;

public class NewArrayExpressionBuilder<T> extends ExpressionBuilder<T, NewArrayExpressionBuilder<T>, NewArrayValueExpressionBuilder<T>>{

  public NewArrayExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(new NewArrayExpressionBuilderFactory<T>(), expressionHandler);
  }

}
