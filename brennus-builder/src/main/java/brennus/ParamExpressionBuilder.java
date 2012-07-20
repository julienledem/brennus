package brennus;


public class ParamExpressionBuilder<T, VEB> extends ExpressionBuilder<MethodCallBuilder<T, VEB>, ParamExpressionBuilder<T, VEB>, ParamValueExpressionBuilder<T, VEB>> {

  ParamExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, VEB>> expressionHandler) {
    super(new ParamExpressionBuilderFactory<T, VEB>(), expressionHandler);
  }

}
