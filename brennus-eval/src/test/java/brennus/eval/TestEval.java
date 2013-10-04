package brennus.eval;

import static brennus.eval.DynamicExpression.BinaryExpression.ArithmeticOperator.MINUS;
import static brennus.eval.DynamicExpression.BinaryExpression.ArithmeticOperator.PLUS;
import static brennus.eval.DynamicExpression.Type.BOOLEAN;
import static brennus.eval.DynamicExpression.Type.INT;
import static brennus.eval.DynamicExpression.UnaryExpression.UnaryOperator.NOT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import brennus.eval.DynamicExpression.BinaryExpression;
import brennus.eval.DynamicExpression.BinaryExpression.ArithmeticOperator;
import brennus.eval.DynamicExpression.BinaryExpression.BooleanOperator;
import brennus.eval.DynamicExpression.BinaryExpression.ComparisonOperator;
import brennus.eval.DynamicExpression.CastExpression;
import brennus.eval.DynamicExpression.ConstantExpression;
import brennus.eval.DynamicExpression.ParamExpression;
import brennus.eval.DynamicExpression.Type;
import brennus.eval.DynamicExpression.UnaryExpression;
import brennus.eval.DynamicExpression.UnaryExpression.UnaryOperator;
import brennus.eval.Parameters.ParametersArray;

import org.junit.Test;

public class TestEval {

  private ExpressionCompiler compiler = new ExpressionCompiler();

  @Test
  public void testConst() {
    BinaryExpression e1 = new BinaryExpression(PLUS, new ConstantExpression(INT, 1), new ConstantExpression(INT, 1));
    assertEquals(2, e1.evalInt(null));
    assertEquals(2, compiler.compileExpression(e1).evalInt(null));
  }

  @Test
  public void testParam() {
    BinaryExpression e1 = new BinaryExpression(PLUS, new ParamExpression(INT, 0), new ConstantExpression(INT, 1));
    Expression ce1 = compiler.compileExpression(e1);
    ParametersArray ZERO = new ParametersArray(0);
    ParametersArray ONE = new ParametersArray(1);
    assertEquals(1, e1.evalInt(ZERO));
    assertEquals(1, ce1.evalInt(ZERO));
    assertEquals(2, e1.evalInt(ONE));
    assertEquals(2, ce1.evalInt(ONE));
  }

  @Test
  public void testArithmetic() {
    for (ArithmeticOperator op : ArithmeticOperator.values()) {
      for (Type t : Type.numericTypes) {
        Object v;
        switch (t) {
        case INT: v = 10; break;
        case LONG: v = 10l; break;
        case FLOAT: v = 10f; break;
        case DOUBLE: v = 10d; break;
        default: throw new UnsupportedOperationException(t.name());
        }
        BinaryExpression e1 = new BinaryExpression(op, new ConstantExpression(t, v), new ConstantExpression(t, v));
        Expression ce1 = compiler.compileExpression(e1);
        assertEquals(eval(t, e1, null), eval(t, ce1, null));
      }
    }
  }

  @Test
  public void testBool() {
    for (BooleanOperator op : BooleanOperator.values()) {
      BinaryExpression e1 = new BinaryExpression(op, new ParamExpression(BOOLEAN, 0), new ParamExpression(BOOLEAN, 1));
      Expression ce1 = compiler.compileExpression(e1);
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          Parameters p = new ParametersArray(i == 0, j == 0);
          assertEquals(e1.evalBoolean(p), ce1.evalBoolean(p));
        }
      }
    }
  }

  @Test
  public void testNot() {
      UnaryExpression e1 = new UnaryExpression(NOT, new ParamExpression(BOOLEAN, 0));
      Expression ce1 = compiler.compileExpression(e1);
      for (int i = 0; i < 2; i++) {
        Parameters p = new ParametersArray(i == 0);
        assertEquals(e1.evalBoolean(p), ce1.evalBoolean(p));
      }
  }

  @Test
  public void testMinus() {
    for (Type t : Type.numericTypes) {
      Object v;
      switch (t) {
      case INT: v = 10; break;
      case LONG: v = 10l; break;
      case FLOAT: v = 10f; break;
      case DOUBLE: v = 10d; break;
      default: throw new UnsupportedOperationException(t.name());
      }
      UnaryExpression e1 = new UnaryExpression(UnaryOperator.MINUS, new ConstantExpression(t, v));
      Expression ce1 = compiler.compileExpression(e1);
      assertEquals(eval(t, e1, null), eval(t, ce1, null));
    }
  }

  @Test
  public void testCast() {
    for (Type t1 : Type.values()) {
      for (Type t2 : Type.values()) {
        Object v;
        switch (t2) {
        case INT: v = 10; break;
        case LONG: v = 10l; break;
        case FLOAT: v = 10f; break;
        case DOUBLE: v = 10d; break;
        case BOOLEAN: v = false; break;
        default: throw new UnsupportedOperationException(t2.name());
        }
        CastExpression e1 = new CastExpression(t1, new ConstantExpression(t2, v));
        Expression ce1 = compiler.compileExpression(e1);
        assertEquals(eval(t1, e1, null), eval(t1, ce1, null));
      }
    }
  }

  @Test
  public void testComp() {
    for (ComparisonOperator op : ComparisonOperator.values()) {
      for (Type t : Type.numericTypes) {
        Object v1, v2;
        switch (t) {
        case INT: v1 = 10; v2 = 20; break;
        case LONG: v1 = 10l; v2 = 20l; break;
        case FLOAT: v1 = 10f; v2 = 20f; break;
        case DOUBLE: v1 = 10d; v2 = 20d; break;
        default: throw new UnsupportedOperationException(t.name());
        }
        BinaryExpression e1 = new BinaryExpression(op, new ConstantExpression(t, v1), new ConstantExpression(t, v1));
        Expression ce1 = compiler.compileExpression(e1);
        assertEquals(e1.evalBoolean(null), ce1.evalBoolean(null));

        BinaryExpression e2 = new BinaryExpression(op, new ConstantExpression(t, v1), new ConstantExpression(t, v2));
        Expression ce2 = compiler.compileExpression(e2);
        assertEquals(e2.evalBoolean(null), ce2.evalBoolean(null));
      }
    }
  }

  public Object eval(Type t, Expression e, Parameters p) {
    switch (t) {
    case INT: return e.evalInt(p);
    case LONG: return e.evalLong(p);
    case FLOAT: return e.evalFloat(p);
    case DOUBLE: return e.evalDouble(p);
    case BOOLEAN: return e.evalBoolean(p);
    default: throw new UnsupportedOperationException(t.name());
    }
  }

  static DynamicExpression generateRandomExp(int depth, Type t) {
    if (depth == 0) {
      if (flip(0.5)) {
        return new ConstantExpression(t, randomValue(t));
      } else {
        return new ParamExpression(t, t.ordinal());
      }
    } else {
      switch (t) {
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE: {
        ArithmeticOperator op = random(ArithmeticOperator.values());
        return new BinaryExpression(op, generateRandomExp(depth - 1, t), generateRandomExp(depth - 1, t));
      }
      case BOOLEAN:
        if (flip(0.5)) {
          BooleanOperator op = random(BooleanOperator.values());
          return new BinaryExpression(op, generateRandomExp(depth - 1, t), generateRandomExp(depth - 1, t));
        } else {
          ComparisonOperator op = random(ComparisonOperator.values());
          Type t2 = random(Type.numericTypes);
          return new BinaryExpression(op, generateRandomExp(depth - 1, t2), generateRandomExp(depth - 1, t2));
        }
      default:
        throw new UnsupportedOperationException(t.name());
      }
    }

  }

  @Test
  public void testComplexExp() {
    BinaryExpression e =
      new BinaryExpression(
          MINUS,
          new BinaryExpression(
              MINUS,
              new ParamExpression(INT, 0),
              new ConstantExpression(INT, 453)),
          new BinaryExpression(
              MINUS,
              new ConstantExpression(INT, 53),
              new ConstantExpression(INT, 427)));
    Expression ce = compiler.compileExpression(e);
    ParametersArray p = new ParametersArray(165);
    assertEquals(e.evalInt(p), ce.evalInt(p));
  }

  @Test
  public void testRandom() {
//    Logger.getLogger("brennus").setLevel(Level.FINEST);
//    Logger.getLogger("brennus").addHandler(new Handler() {
//      public void publish(LogRecord record) {
//        System.out.println(record.getMessage());
//      }
//      public void flush() {
//        System.out.flush();
//      }
//      public void close() throws SecurityException {
//        System.out.flush();
//      }
//    });
    Object[] values = new Object[Type.values().length];
    for (int i = 0; i < values.length; i++) {
      values[i] = randomValue(Type.values()[i]);
    }
    ParametersArray p = new ParametersArray(values);
    for (Type t : Type.values()) {
      DynamicExpression e = generateRandomExp(5, t);
      System.out.println(p);
      System.out.println(e);
      Expression ce = compiler.compileExpression(e);
      try {
        Object expected = eval(t, e, p);
        try {
          assertEquals(expected, eval(t, ce, p));
        } catch (ArithmeticException ex) {
          fail(ex.toString());
        }
      } catch (ArithmeticException ex) {
        System.out.println(ex);
        try {
          eval(t, ce, p);
          fail("expected: " + ex);
        } catch (ArithmeticException ex2) {
          assertEquals(ex.toString(), ex2.toString());
        }
      }
    }
  }

  static Object randomValue(Type t) {
    double r = Math.random() * 1000;
    switch (t) {
    case INT: return (int)r;
    case LONG: return (long)r;
    case FLOAT: return (float)r;
    case DOUBLE: return r;
    case BOOLEAN: return (r < 500);
    default: throw new UnsupportedOperationException(t.name());
    }
  }

  static <T> T random(T[] vals) {
    return vals[(int)(Math.random() * vals.length)];
  }

  static boolean flip(double probability) {
     return Math.random() < probability;
  }

}

