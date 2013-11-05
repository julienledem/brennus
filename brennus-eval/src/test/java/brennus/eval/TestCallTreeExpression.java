package brennus.eval;

import static brennus.model.ExistingType.LONG;
import junit.framework.Assert;

import org.junit.Test;

import brennus.asm.DynamicClassLoader;
import brennus.eval.CallTreeExpression.*;
import static brennus.eval.CallTreeExpression.*;
import brennus.model.FutureType;

public class TestCallTreeExpression {

  private DynamicClassLoader cl = new DynamicClassLoader();

  @Test
  public void test() throws Exception {
    MethodCallExpression exp = call("plusLong", lit(1l), lit(2l));
    FutureType t = exp.compile(GeneratedExpression.class, LONG, "evalLong", Parameters.class);
    cl.define(t);
    Expression compiled = (Expression)cl.loadClass(t.getName()).newInstance();
    Assert.assertEquals(3, compiled.evalLong(null));
  }

}
