package brennus.eval;

import static brennus.eval.DynamicExpression.Type.BOOLEAN;
import brennus.eval.DynamicExpression.BinaryExpression.ArithmeticOperator;
import brennus.eval.DynamicExpression.BinaryExpression.BooleanOperator;
import brennus.eval.DynamicExpression.BinaryExpression.ComparisonOperator;


public abstract class DynamicExpression extends Expression {

  public final Type returnType;

  public DynamicExpression(Type returnType) {
    this.returnType = returnType;
  }

  public abstract <T> T accept(ExpressionVisitor<T> v);

  public static enum Type {
    INT, LONG,
    FLOAT, DOUBLE,
    BOOLEAN;
    //  TODO: immutable
    static final public Type[] numericTypes = { INT, LONG, FLOAT, DOUBLE };
  }

  public static final class BinaryExpression extends DynamicExpression {

    public final BinaryOperator operator;
    public final DynamicExpression left;
    public final DynamicExpression right;

    public BinaryExpression(BinaryOperator operator, DynamicExpression left, DynamicExpression right) {
      super(operator.validateTypes(left, right));
      this.operator = operator;
      this.left = left;
      this.right = right;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
      return v.visit(this);
    }

    @Override
    public String toString() {
      return operator + "(" + left + ", " + right + ")";
    }

    @Override
    public int evalInt(Parameters p) {
      return operator.evalInt(left, right, p);
    }
    @Override
    public long evalLong(Parameters p) {
      return operator.evalLong(left, right, p);
    }
    @Override
    public float evalFloat(Parameters p) {
      return operator.evalFloat(left, right, p);
    }
    @Override
    public double evalDouble(Parameters p) {
      return operator.evalDouble(left, right, p);
    }
    @Override
    public boolean evalBoolean(Parameters p) {
      return operator.evalBoolean(left, right, p);
    }

    public static interface BinaryOperator {

      public <T> T accept(BinaryOperatorVisitor<T> v);

      public Type validateTypes(DynamicExpression left, DynamicExpression right);

      public int evalInt(DynamicExpression left, DynamicExpression right, Parameters p);
      public long evalLong(DynamicExpression left, DynamicExpression right, Parameters p);
      public float evalFloat(DynamicExpression left, DynamicExpression right, Parameters p);
      public double evalDouble(DynamicExpression left, DynamicExpression right, Parameters p);
      public boolean evalBoolean(DynamicExpression left, DynamicExpression right, Parameters p);

    }

    public static enum ArithmeticOperator implements BinaryOperator {
      PLUS {
        public int eval(int left, int right) {
          return left + right;
        }
        public long eval(long left, long right) {
          return left + right;
        }
        public float eval(float left, float right) {
          return left + right;
        }
        public double eval(double left, double right) {
          return left + right;
        }
      },
      MINUS {
        public int eval(int left, int right) {
          return left - right;
        }
        public long eval(long left, long right) {
          return left - right;
        }
        public float eval(float left, float right) {
          return left - right;
        }
        public double eval(double left, double right) {
          return left - right;
        }
      },
      MULTIPLY {
        public int eval(int left, int right) {
          return left * right;
        }
        public long eval(long left, long right) {
          return left * right;
        }
        public float eval(float left, float right) {
          return left * right;
        }
        public double eval(double left, double right) {
          return left * right;
        }
      },
      DIVIDE {
        public int eval(int left, int right) {
          return left / right;
        }
        public long eval(long left, long right) {
          return left / right;
        }
        public float eval(float left, float right) {
          return left / right;
        }
        public double eval(double left, double right) {
          return left / right;
        }
      };

      public abstract int eval(int left, int right);
      public abstract long eval(long left, long right);
      public abstract float eval(float left, float right);
      public abstract double eval(double left, double right);

      public int evalInt(DynamicExpression left, DynamicExpression right, Parameters p) {
        return eval(left.evalInt(p), right.evalInt(p));
      }
      public long evalLong(DynamicExpression left, DynamicExpression right, Parameters p) {
        return eval(left.evalLong(p), right.evalLong(p));
      }
      public float evalFloat(DynamicExpression left, DynamicExpression right, Parameters p) {
        return eval(left.evalFloat(p), right.evalFloat(p));
      }
      public double evalDouble(DynamicExpression left, DynamicExpression right, Parameters p) {
        return eval(left.evalDouble(p), right.evalDouble(p));
      }
      public boolean evalBoolean(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }

      public <T> T accept(BinaryOperatorVisitor<T> v) {
        return v.visit(this);
      }

      @Override
      public Type validateTypes(DynamicExpression left, DynamicExpression right) {
        if (left.returnType != right.returnType) {
          throw new IllegalArgumentException(left.returnType + " " + right.returnType + " should be the same");
        }
        return left.returnType;
      }

    }

    public static enum BooleanOperator implements BinaryOperator {
      AND {
        public boolean eval(boolean left, DynamicExpression right, Parameters parameters) {
          return left && right.evalBoolean(parameters);
        }
      },
      OR {
        public boolean eval(boolean left, DynamicExpression right, Parameters parameters) {
          return left || right.evalBoolean(parameters);
        }
      },
      XOR {
        public boolean eval(boolean left, DynamicExpression right, Parameters parameters) {
          return left ^ right.evalBoolean(parameters);
        }
      };

      public int evalInt(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public long evalLong(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public float evalFloat(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public double evalDouble(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public boolean evalBoolean(DynamicExpression left, DynamicExpression right, Parameters p) {
        return eval(left.evalBoolean(p), right, p);
      }

      protected abstract boolean eval(boolean eval, DynamicExpression right, Parameters parameters);

      public <T> T accept(BinaryOperatorVisitor<T> v) {
        return v.visit(this);
      }

      @Override
      public Type validateTypes(DynamicExpression left, DynamicExpression right) {
        if (left.returnType != BOOLEAN || right.returnType != BOOLEAN) {
          throw new IllegalArgumentException(left.returnType + " and " + right.returnType + " should both be BOOLEAN");
        }
        return BOOLEAN;
      }

    }
    public static enum ComparisonOperator implements BinaryOperator {
      GT {
        public boolean eval(int left, int right) {
          return left > right;
        }
        public boolean eval(long left, long right) {
          return left > right;
        }
        public boolean eval(float left, float right) {
          return left > right;
        }
        public boolean eval(double left, double right) {
          return left > right;
        }
      },
      LT {
        public boolean eval(int left, int right) {
          return left < right;
        }
        public boolean eval(long left, long right) {
          return left < right;
        }
        public boolean eval(float left, float right) {
          return left < right;
        }
        public boolean eval(double left, double right) {
          return left < right;
        }
      },
      EQ {
        public boolean eval(int left, int right) {
          return left == right;
        }
        public boolean eval(long left, long right) {
          return left == right;
        }
        public boolean eval(float left, float right) {
          return left == right;
        }
        public boolean eval(double left, double right) {
          return left == right;
        }
      },
      GE {
        public boolean eval(int left, int right) {
          return left >= right;
        }
        public boolean eval(long left, long right) {
          return left >= right;
        }
        public boolean eval(float left, float right) {
          return left >= right;
        }
        public boolean eval(double left, double right) {
          return left >= right;
        }
      },
      LE {
        public boolean eval(int left, int right) {
          return left <= right;
        }
        public boolean eval(long left, long right) {
          return left <= right;
        }
        public boolean eval(float left, float right) {
          return left <= right;
        }
        public boolean eval(double left, double right) {
          return left <= right;
        }
      },
      NE {
        public boolean eval(int left, int right) {
          return left != right;
        }
        public boolean eval(long left, long right) {
          return left != right;
        }
        public boolean eval(float left, float right) {
          return left != right;
        }
        public boolean eval(double left, double right) {
          return left != right;
        }
      };

      protected abstract boolean eval(int left, int right);
      protected abstract boolean eval(long left, long right);
      protected abstract boolean eval(float left, float right);
      protected abstract boolean eval(double left, double right);

      @Override
      public Type validateTypes(DynamicExpression left, DynamicExpression right) {
        if (left.returnType != right.returnType) {
          throw new UnsupportedOperationException(left.returnType + " != " + right.returnType);
        }
        return BOOLEAN;
      }

      public int evalInt(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public long evalLong(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public float evalFloat(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public double evalDouble(DynamicExpression left, DynamicExpression right, Parameters p) {
        throw new UnsupportedOperationException();
      }
      public boolean evalBoolean(DynamicExpression left, DynamicExpression right, Parameters p) {
        switch(left.returnType) {
        case INT: return eval(left.evalInt(p), right.evalInt(p));
        case LONG: return eval(left.evalLong(p), right.evalLong(p));
        case FLOAT: return eval(left.evalFloat(p), right.evalFloat(p));
        case DOUBLE: return eval(left.evalDouble(p), right.evalDouble(p));
        default:
          throw new UnsupportedOperationException(left.returnType.name());
        }
      }

      public <T> T accept(BinaryOperatorVisitor<T> v) {
        return v.visit(this);
      }

    }

  }

  public interface BinaryOperatorVisitor<T> {

    T visit(ArithmeticOperator o);

    T visit(ComparisonOperator o);

    T visit(BooleanOperator o);

  }

  public static final class UnaryExpression extends DynamicExpression {

    public final UnaryOperator operator;
    public final DynamicExpression operand;

    public UnaryExpression(UnaryOperator operator, DynamicExpression operand) {
      super(operator.validateType(operand));
      this.operator = operator;
      this.operand = operand;
    }

    @Override
    public int evalInt(Parameters p) {
      return operator.evalInt(operand.evalInt(p));
    }
    @Override
    public long evalLong(Parameters p) {
      return operator.evalLong(operand.evalLong(p));
    }
    @Override
    public float evalFloat(Parameters p) {
      return operator.evalFloat(operand.evalFloat(p));
    }
    @Override
    public double evalDouble(Parameters p) {
      return operator.evalDouble(operand.evalDouble(p));
    }
    @Override
    public boolean evalBoolean(Parameters p) {
      return operator.evalBoolean(operand.evalBoolean(p));
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
      return v.visit(this);
    }

    public static enum UnaryOperator {
      MINUS {

        @Override
        public Type validateType(DynamicExpression operand) {
          if (operand.returnType == BOOLEAN) {
            throw new IllegalArgumentException("can't minus BOOLEAN");
          }
          return operand.returnType;
        }

        @Override
        public int evalInt(int val) {
          return - val;
        }
        @Override
        public long evalLong(long val) {
          return - val;
        }
        @Override
        public float evalFloat(float val) {
          return - val;
        }
        @Override
        public double evalDouble(double val) {
          return - val;
        }
        @Override
        public boolean evalBoolean(boolean val) {
          throw new UnsupportedOperationException();
        }
      },
      NOT {
        @Override
        public Type validateType(DynamicExpression operand) {
          if (operand.returnType != BOOLEAN) {
            throw new IllegalArgumentException("can't not " + operand.returnType);
          }
          return operand.returnType;
        }
        @Override
        public int evalInt(int val) {
          throw new UnsupportedOperationException();
        }
        @Override
        public long evalLong(long val) {
          throw new UnsupportedOperationException();
        }
        @Override
        public float evalFloat(float val) {
          throw new UnsupportedOperationException();
        }
        @Override
        public double evalDouble(double val) {
          throw new UnsupportedOperationException();
        }
        @Override
        public boolean evalBoolean(boolean val) {
          return ! val;
        }
      };

      public abstract Type validateType(DynamicExpression operand);

      public abstract int evalInt(int val);
      public abstract long evalLong(long val);
      public abstract float evalFloat(float val);
      public abstract double evalDouble(double val);
      public abstract boolean evalBoolean(boolean val);

    }

  }

  public static final class ConstantExpression extends DynamicExpression {

    public final Object value;

    public ConstantExpression(Type type, Object value) {
      super(type);
      this.value = value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
      return v.visit(this);
    }

    @Override
    public String toString() {
      return value.toString() + returnType.name();
    }

    @Override
    public int evalInt(Parameters p) {
      return (Integer) value;
    }

    @Override
    public long evalLong(Parameters p) {
      return (Long) value;
    }

    @Override
    public float evalFloat(Parameters p) {
      return (Float) value;
    }

    @Override
    public double evalDouble(Parameters p) {
      return (Double) value;
    }

    @Override
    public boolean evalBoolean(Parameters p) {
      return (Boolean) value;
    }

  }

  public static final class ParamExpression extends DynamicExpression {

    public final int index;

    public ParamExpression(Type type, int index) {
      super(type);
      this.index = index;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
      return v.visit(this);
    }

    @Override
    public String toString() {
      return returnType.name() + "[" + index + "]";
    }

    @Override
    public int evalInt(Parameters p) {
      return p.getInt(index);
    }

    @Override
    public long evalLong(Parameters p) {
      return p.getLong(index);
    }

    @Override
    public float evalFloat(Parameters p) {
      return p.getFloat(index);
    }

    @Override
    public double evalDouble(Parameters p) {
      return p.getDouble(index);
    }

    @Override
    public boolean evalBoolean(Parameters p) {
      return p.getBoolean(index);
    }

  }

  public static final class CastExpression extends DynamicExpression {

    public final DynamicExpression castedExpression;

    public CastExpression(Type type, DynamicExpression castedExpression) {
      super(type);
      this.castedExpression = castedExpression;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
      return v.visit(this);
    }

    @Override
    public String toString() {
      return "(" + returnType + ")" + castedExpression.toString();
    }

    @Override
    public int evalInt(Parameters p) {
      switch(castedExpression.returnType) {
      case INT: return castedExpression.evalInt(p);
      case LONG: return (int)castedExpression.evalLong(p);
      case FLOAT: return (int)castedExpression.evalFloat(p);
      case DOUBLE: return (int)castedExpression.evalDouble(p);
      case BOOLEAN: return castedExpression.evalBoolean(p) ? 0 : 1;
      default:
        throw new UnsupportedOperationException(castedExpression.returnType.name());
      }
    }

    @Override
    public long evalLong(Parameters p) {
      switch(castedExpression.returnType) {
      case INT: return (long)castedExpression.evalInt(p);
      case LONG: return castedExpression.evalLong(p);
      case FLOAT: return (long)castedExpression.evalFloat(p);
      case DOUBLE: return (long)castedExpression.evalDouble(p);
      case BOOLEAN: return castedExpression.evalBoolean(p) ? 0l : 1l;
      default:
        throw new UnsupportedOperationException(castedExpression.returnType.name());
      }
    }

    @Override
    public float evalFloat(Parameters p) {
      switch(castedExpression.returnType) {
      case INT: return (float)castedExpression.evalInt(p);
      case LONG: return (float)castedExpression.evalLong(p);
      case FLOAT: return castedExpression.evalFloat(p);
      case DOUBLE: return (float)castedExpression.evalDouble(p);
      case BOOLEAN: return castedExpression.evalBoolean(p) ? 0f : 1f;
      default:
        throw new UnsupportedOperationException(castedExpression.returnType.name());
      }
    }

    @Override
    public double evalDouble(Parameters p) {
      switch(castedExpression.returnType) {
      case INT: return (double)castedExpression.evalInt(p);
      case LONG: return (double)castedExpression.evalLong(p);
      case FLOAT: return (double)castedExpression.evalFloat(p);
      case DOUBLE: return castedExpression.evalDouble(p);
      case BOOLEAN: return castedExpression.evalBoolean(p) ? 0d : 1d;
      default:
        throw new UnsupportedOperationException(castedExpression.returnType.name());
      }
    }

    @Override
    public boolean evalBoolean(Parameters p) {
      switch(castedExpression.returnType) {
      case INT: return castedExpression.evalInt(p) == 0;
      case LONG: return castedExpression.evalLong(p) == 0l;
      case FLOAT: return castedExpression.evalFloat(p) == 0f;
      case DOUBLE: return castedExpression.evalDouble(p) == 0d;
      case BOOLEAN: return castedExpression.evalBoolean(p);
      default:
        throw new UnsupportedOperationException(castedExpression.returnType.name());
      }
    }

  }

  public interface ExpressionVisitor<T> {

    T visit(BinaryExpression e);

    T visit(CastExpression e);

    T visit(ParamExpression e);

    T visit(ConstantExpression e);

    T visit(UnaryExpression e);

  }

}
