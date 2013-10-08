package brennus.eval;

public abstract class GeneratedExpression extends Expression {

  final public int plusInt(int left, int right) {
    return left + right;
  }
  final public long plusLong(long left, long right) {
    return left + right;
  }
  final public float plusFloat(float left, float right) {
    return left + right;
  }
  final public double plusDouble(double left, double right) {
    return left + right;
  }
  final public int minusInt(int left, int right) {
    return left - right;
  }
  final public long minusLong(long left, long right) {
    return left - right;
  }
  final public float minusFloat(float left, float right) {
    return left - right;
  }
  final public double minusDouble(double left, double right) {
    return left - right;
  }
  final public int multiplyInt(int left, int right) {
    return left * right;
  }
  final public long multiplyLong(long left, long right) {
    return left * right;
  }
  final public float multiplyFloat(float left, float right) {
    return left * right;
  }
  final public double multiplyDouble(double left, double right) {
    return left * right;
  }
  final public int divideInt(int left, int right) {
    return left / right;
  }
  final public long divideLong(long left, long right) {
    return left / right;
  }
  final public float divideFloat(float left, float right) {
    return left / right;
  }
  final public double divideDouble(double left, double right) {
    return left / right;
  }

  // TODO: these should be implemented as ifs in the compiler. Or maybe not
  final public boolean and(boolean left, boolean right) {
    return left && right;
  }
  final public boolean or(boolean left, boolean right) {
    return left || right;
  }
  final public boolean xor(boolean left, boolean right) {
    return left ^ right;
  }

  final public boolean gtInt(int left, int right) {
    return left > right;
  }
  final public boolean gtLong(long left, long right) {
    return left > right;
  }
  final public boolean gtFloat(float left, float right) {
    return left > right;
  }
  final public boolean gtDouble(double left, double right) {
    return left > right;
  }
  final public boolean ltInt(int left, int right) {
    return left < right;
  }
  final public boolean ltLong(long left, long right) {
    return left < right;
  }
  final public boolean ltFloat(float left, float right) {
    return left < right;
  }
  final public boolean ltDouble(double left, double right) {
    return left < right;
  }
  final public boolean eqInt(int left, int right) {
    return left == right;
  }
  final public boolean eqLong(long left, long right) {
    return left == right;
  }
  final public boolean eqFloat(float left, float right) {
    return left == right;
  }
  final public boolean eqDouble(double left, double right) {
    return left == right;
  }
  final public boolean geInt(int left, int right) {
    return left >= right;
  }
  final public boolean geLong(long left, long right) {
    return left >= right;
  }
  final public boolean geFloat(float left, float right) {
    return left >= right;
  }
  final public boolean geDouble(double left, double right) {
    return left >= right;
  }
  final public boolean leInt(int left, int right) {
    return left <= right;
  }
  final public boolean leLong(long left, long right) {
    return left <= right;
  }
  final public boolean leFloat(float left, float right) {
    return left <= right;
  }
  final public boolean leDouble(double left, double right) {
    return left <= right;
  }
  final public boolean neInt(int left, int right) {
    return left != right;
  }
  final public boolean neLong(long left, long right) {
    return left != right;
  }
  final public boolean neFloat(float left, float right) {
    return left != right;
  }
  final public boolean neDouble(double left, double right) {
    return left != right;
  }

  final public boolean not(boolean operand) {
    return !operand;
  }

  final public int minusInt(int operand) {
    return - operand;
  }
  final public long minusLong(long operand) {
    return - operand;
  }
  final public float minusFloat(float operand) {
    return - operand;
  }
  final public double minusDouble(double operand) {
    return - operand;
  }

  final public int castIntInt(int v) {
    return v;
  }
  final public int castIntLong(long v) {
    return (int)v;
  }
  final public int castIntFloat(float v) {
    return (int)v;
  }
  final public int castIntDouble(double v) {
    return (int)v;
  }
  final public int castIntBoolean(boolean v) {
    return v ? 0 : 1;
  }

  final public long castLongInt(int v) {
    return (long)v;
  }
  final public long castLongLong(long v) {
    return v;
  }
  final public long castLongFloat(float v) {
    return (long)v;
  }
  final public long castLongDouble(double v) {
    return (long)v;
  }
  final public long castLongBoolean(boolean v) {
    return v ? 0l : 1l;
  }

  final public float castFloatInt(int v) {
    return (float)v;
  }
  final public float castFloatLong(long v) {
    return (float)v;
  }
  final public float castFloatFloat(float v) {
    return v;
  }
  final public float castFloatDouble(double v) {
    return (float)v;
  }
  final public float castFloatBoolean(boolean v) {
    return v ? 0f : 1f;
  }

  final public double castDoubleInt(int v) {
    return (double)v;
  }
  final public double castDoubleLong(long v) {
    return (double)v;
  }
  final public double castDoubleFloat(float v) {
    return (double)v;
  }
  final public double castDoubleDouble(double v) {
    return v;
  }
  final public double castDoubleBoolean(boolean v) {
    return v ? 0d : 1d;
  }

  final public boolean castBooleanInt(int v) {
    return v == 0;
  }
  final public boolean castBooleanLong(long v) {
    return v == 0l;
  }
  final public boolean castBooleanFloat(float v) {
    return v == 0f;
  }
  final public boolean castBooleanDouble(double v) {
    return v == 0d;
  }
  final public boolean castBooleanBoolean(boolean v) {
    return v;
  }

}
