package brennus.eval;

import static brennus.eval.DynamicExpression.Type.DOUBLE;
import brennus.eval.DynamicExpression.Type;
import brennus.eval.Parameters.ParametersArray;

public class PerfTest {

  private static final String SPACE = "                                                    ";
  private static ExpressionCompiler compiler = new ExpressionCompiler();

  public static void main(String[] args) {
    perfTest();
  }

  public static void perfTest() {
    for (int i = 1; i <= 4; i++) {
      testPerf(i);
    }
  }

  private static void testPerf(int depth) {
    System.out.println(SPACE + "DEPTH: " + depth);
    Object[] values = new Object[Type.values().length];
    for (int i = 0; i < values.length; i++) {
      values[i] = TestEval.randomValue(Type.values()[i]);
    }
    ParametersArray p = new ParametersArray(values);
    Type t = DOUBLE;
    DynamicExpression e = TestEval.generateRandomExp(depth, t);
    System.out.println(p);
    System.out.println(e);
    long t0 = System.currentTimeMillis();
    Expression ce = compiler.compileExpression(e);
    long t1 = System.currentTimeMillis();
    System.out.println(SPACE + "compilation: " + (t1 - t0) +  "ms");
    compare(p, e, ce, 1000000);
    compare(p, e, ce, 1000000);
    compare(p, e, ce, 1000000);
    compare(p, e, ce, 10000000);
  }

  private static void compare(
      ParametersArray p,
      DynamicExpression e, Expression ce, int n) {
    System.out.print("<");
    long t0 = eval(p, e, n);
    System.out.print("><");
    long t1 = eval(p, ce, n);
    System.out.print(">");
    String message = "1E" + (int)Math.log10(n) + " " + msPerMil(n, t0) + " -> " + msPerMil(n, t1) + " ";
    while (message.length() < 25) {
      message += " ";
    }
    if (t0 > 0) {
      message += (((float)(t1 * 10000 / t0))/100) + "%";
    }
    System.out.println(SPACE + message);
  }

  private static String msPerMil(int n, long ms) {
    return ((float)1000000 * ms / n) + "ms";
  }

  public static double tot = 0;

  private static long eval(ParametersArray p, Expression e, int n) {
//    System.gc();
    long t0 = System.currentTimeMillis();
    tot = 0;
    for (int i = 0; i < n; i++) {
      tot += e.evalDouble(p);
    }
    long t1 = System.currentTimeMillis();
    long t = t1 - t0;
    return t;
  }

}
