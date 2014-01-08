package brennus;


public class ArrayIndexExpressionBuilder<T> extends ExpressionBuilder<T, ArrayIndexExpressionBuilder<T>, ArrayIndexValueExpressionBuilder<T>>{

  public ArrayIndexExpressionBuilder(ExpressionHandler<T> expressionHandler) {
    super(new ArrayIndexExpressionBuilderFactory<T>(), expressionHandler);
  }

}
