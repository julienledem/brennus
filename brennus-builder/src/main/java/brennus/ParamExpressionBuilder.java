package brennus;


public final class ParamExpressionBuilder<T, EB, VEB> extends ExpressionBuilder<MethodCallBuilder<T, EB, VEB>, ParamExpressionBuilder<T, EB, VEB>, ParamValueExpressionBuilder<T, EB, VEB>> {

  ParamExpressionBuilder(
      ExpressionHandler<MethodCallBuilder<T, EB, VEB>> expressionHandler) {
    super(new ParamExpressionBuilderFactory<T, EB, VEB>(), expressionHandler);
  }

}
