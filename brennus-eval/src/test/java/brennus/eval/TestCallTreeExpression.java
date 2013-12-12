package brennus.eval;

import static brennus.eval.CallTreeExpression.call;
import static brennus.eval.CallTreeExpression.lit;
import static brennus.model.ExistingType.LONG;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import junit.framework.Assert;
import brennus.Builder;
import brennus.Function;
import brennus.MethodBuilder;
import brennus.MethodDeclarationBuilder;
import brennus.ReturnExpressionBuilder;
import brennus.ReturnValueExpressionBuilder;
import brennus.asm.DynamicClassLoader;
import brennus.eval.CallTreeExpression.MethodCallExpression;
import brennus.model.FutureType;
import brennus.model.Type;

import org.junit.Test;

public class TestCallTreeExpression {

  private DynamicClassLoader cl = new DynamicClassLoader();

  @Test
  public void test() throws Exception {
    MethodCallExpression exp = call("plusLong", lit(1l), lit(2l));
    FutureType t = compileToClass(exp, GeneratedExpression.class, LONG, "evalLong", Parameters.class);
    cl.define(t);
    Expression compiled = (Expression)cl.loadClass(t.getName()).newInstance();
    Assert.assertEquals(3, compiled.evalLong(null));
  }

  private static int id = 0;


  public FutureType compileToClass(final MethodCallExpression exp, Class<?> parent, Type returnType, String methodName, final Class<?>... parameters) {
    FutureType c = new Builder()
      .startClass("brennus.eval.CallTreeExpression$Expression" + (++id), existing(parent))
      .startMethod(PUBLIC, returnType, methodName)
        .map(new Function<MethodDeclarationBuilder, MethodDeclarationBuilder>() {
          @Override
          public MethodDeclarationBuilder apply(MethodDeclarationBuilder input) {
            int paramNumber = 0;
            for (Class<?> p : parameters) {
              input = input.param(existing(p), "arg" + (paramNumber++));
            }
            return input;
          }
        })
          .returnExp()
            .map(new Function<ReturnExpressionBuilder<MethodBuilder>, ReturnValueExpressionBuilder<MethodBuilder>>() {
              @Override
              public ReturnValueExpressionBuilder<MethodBuilder> apply(ReturnExpressionBuilder<MethodBuilder> input) {
                return exp.compileToExpression(input);
              }
            })
          .endReturn()
        .endMethod()
      .endClass();
    return c;

  }

}
