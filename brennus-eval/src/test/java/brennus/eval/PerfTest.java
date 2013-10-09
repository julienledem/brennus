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

  private static final int entrySize = 10000;

  private static void testPerf(int depth) {
    System.out.println(SPACE + "DEPTH: " + depth);
    ParametersArray[] p = new ParametersArray[entrySize];
    for (int i = 0; i < entrySize; i++) {
      Object[] values = new Object[Type.values().length];
      for (int j = 0; j < values.length; j++) {
        values[j] = TestEval.randomValue(Type.values()[j]);
      }
      p[i] = new ParametersArray(values);
    }
    Type t = DOUBLE;
    DynamicExpression e = TestEval.generateRandomExp(depth, t);
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
      ParametersArray[] p,
      DynamicExpression e, Expression ce, int n) {
//  System.gc();
    System.out.print("<");
    long t0 = eval(p, e, n);
    double tot1 = tot;
    System.out.print("><");
    long t1 = eval(p, ce, n);
    double tot2 = tot;
    System.out.print(">");
    String message = "1E" + (int)Math.log10(n) + " " + msPerMil(n, t0) + " -> " + msPerMil(n, t1) + " ";
    while (message.length() < 25) {
      message += " ";
    }
    if (t0 > 0) {
      message += (((float)(t1 * 10000 / t0))/100) + "%";
    }
    final double diff = tot2 - tot1;
    System.out.println(SPACE + message + (diff != 0.0 ? " !!! " + diff + " = " + tot2 + " - " + tot1 : ""));
  }

  private static String msPerMil(int n, long ms) {
    return ((float)1000000 * ms / n) + "ms";
  }

  public static double tot = 0;

  private static long eval(ParametersArray[] p, Expression e, int n) {
    long t0 = System.currentTimeMillis();
    tot = 0;
    for (int i = 0; i < n; i++) {
      tot += e.evalDouble(p[i % entrySize]);
    }
    long t1 = System.currentTimeMillis();
    long t = t1 - t0;
    return t;
  }

}
