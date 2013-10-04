package brennus.eval;

abstract public class Expression {

  public int plusInt(int left, int right) {
    return left + right;
  }
  public long plusLong(long left, long right) {
    return left + right;
  }
  public float plusFloat(float left, float right) {
    return left + right;
  }
  public double plusDouble(double left, double right) {
    return left + right;
  }
  public int minusInt(int left, int right) {
    return left - right;
  }
  public long minusLong(long left, long right) {
    return left - right;
  }
  public float minusFloat(float left, float right) {
    return left - right;
  }
  public double minusDouble(double left, double right) {
    return left - right;
  }
  public int multiplyInt(int left, int right) {
    return left * right;
  }
  public long multiplyLong(long left, long right) {
    return left * right;
  }
  public float multiplyFloat(float left, float right) {
    return left * right;
  }
  public double multiplyDouble(double left, double right) {
    return left * right;
  }
  public int divideInt(int left, int right) {
    return left / right;
  }
  public long divideLong(long left, long right) {
    return left / right;
  }
  public float divideFloat(float left, float right) {
    return left / right;
  }
  public double divideDouble(double left, double right) {
    return left / right;
  }

  // TODO: these should be implemented as ifs in the compiler. Or maybe not
  public boolean and(boolean left, boolean right) {
    return left && right;
  }
  public boolean or(boolean left, boolean right) {
    return left || right;
  }
  public boolean xor(boolean left, boolean right) {
    return left ^ right;
  }

  public boolean gtInt(int left, int right) {
    return left > right;
  }
  public boolean gtLong(long left, long right) {
    return left > right;
  }
  public boolean gtFloat(float left, float right) {
    return left > right;
  }
  public boolean gtDouble(double left, double right) {
    return left > right;
  }
  public boolean ltInt(int left, int right) {
    return left < right;
  }
  public boolean ltLong(long left, long right) {
    return left < right;
  }
  public boolean ltFloat(float left, float right) {
    return left < right;
  }
  public boolean ltDouble(double left, double right) {
    return left < right;
  }
  public boolean eqInt(int left, int right) {
    return left == right;
  }
  public boolean eqLong(long left, long right) {
    return left == right;
  }
  public boolean eqFloat(float left, float right) {
    return left == right;
  }
  public boolean eqDouble(double left, double right) {
    return left == right;
  }
  public boolean geInt(int left, int right) {
    return left >= right;
  }
  public boolean geLong(long left, long right) {
    return left >= right;
  }
  public boolean geFloat(float left, float right) {
    return left >= right;
  }
  public boolean geDouble(double left, double right) {
    return left >= right;
  }
  public boolean leInt(int left, int right) {
    return left <= right;
  }
  public boolean leLong(long left, long right) {
    return left <= right;
  }
  public boolean leFloat(float left, float right) {
    return left <= right;
  }
  public boolean leDouble(double left, double right) {
    return left <= right;
  }
  public boolean neInt(int left, int right) {
    return left != right;
  }
  public boolean neLong(long left, long right) {
    return left != right;
  }
  public boolean neFloat(float left, float right) {
    return left != right;
  }
  public boolean neDouble(double left, double right) {
    return left != right;
  }

  public boolean not(boolean operand) {
    return !operand;
  }

  public int minusInt(int operand) {
    return - operand;
  }
  public long minusLong(long operand) {
    return - operand;
  }
  public float minusFloat(float operand) {
    return - operand;
  }
  public double minusDouble(double operand) {
    return - operand;
  }

  public int castIntInt(int v) {
    return v;
  }
  public int castIntLong(long v) {
    return (int)v;
  }
  public int castIntFloat(float v) {
    return (int)v;
  }
  public int castIntDouble(double v) {
    return (int)v;
  }
  public int castIntBoolean(boolean v) {
    return v ? 0 : 1;
  }

  public long castLongInt(int v) {
    return (long)v;
  }
  public long castLongLong(long v) {
    return v;
  }
  public long castLongFloat(float v) {
    return (long)v;
  }
  public long castLongDouble(double v) {
    return (long)v;
  }
  public long castLongBoolean(boolean v) {
    return v ? 0l : 1l;
  }

  public float castFloatInt(int v) {
    return (float)v;
  }
  public float castFloatLong(long v) {
    return (float)v;
  }
  public float castFloatFloat(float v) {
    return v;
  }
  public float castFloatDouble(double v) {
    return (float)v;
  }
  public float castFloatBoolean(boolean v) {
    return v ? 0f : 1f;
  }

  public double castDoubleInt(int v) {
    return (double)v;
  }
  public double castDoubleLong(long v) {
    return (double)v;
  }
  public double castDoubleFloat(float v) {
    return (double)v;
  }
  public double castDoubleDouble(double v) {
    return v;
  }
  public double castDoubleBoolean(boolean v) {
    return v ? 0d : 1d;
  }

  public boolean castBooleanInt(int v) {
    return v == 0;
  }
  public boolean castBooleanLong(long v) {
    return v == 0l;
  }
  public boolean castBooleanFloat(float v) {
    return v == 0f;
  }
  public boolean castBooleanDouble(double v) {
    return v == 0d;
  }
  public boolean castBooleanBoolean(boolean v) {
    return v;
  }

  public int evalInt(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public long evalLong(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public float evalFloat(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public double evalDouble(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public boolean evalBoolean(Parameters p) {
    throw new UnsupportedOperationException();
  }
}
