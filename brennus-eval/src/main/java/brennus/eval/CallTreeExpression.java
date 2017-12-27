package brennus.eval;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import brennus.ExpressionBuilder;
import brennus.Function;
import brennus.ParamExpressionBuilder;
import brennus.ParamValueExpressionBuilder;
import brennus.ValueExpressionBuilder;

abstract public class CallTreeExpression extends Expression {

  public static MethodCallExpression call(String methodName, CallTreeExpression... children) {
    return new MethodCallExpression(methodName, children);
  }

  public static ParamExpression param(String name) {
    return new ParamExpression(name);
  }

  public static IntLiteralExpression lit(int value) {
    return new IntLiteralExpression(value);
  }
  public static LongLiteralExpression lit(long value) {
    return new LongLiteralExpression(value);
  }
  public static FloatLiteralExpression lit(float value) {
    return new FloatLiteralExpression(value);
  }
  public static DoubleLiteralExpression lit(double value) {
    return new DoubleLiteralExpression(value);
  }
  public static StringLiteralExpression lit(String value) {
    return new StringLiteralExpression(value);
  }
  public static BooleanLiteralExpression lit(boolean value) {
    return new BooleanLiteralExpression(value);
  }

  public abstract <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder);

  public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> Function<EB, VEB> toFunc() {
    return new Function<EB, VEB>() {
      public VEB apply(EB eb) {
        return compileToExpression(eb);
      }
    };
  }

  public static class MethodCallExpression extends CallTreeExpression {
    private final String methodName;
    private final List<CallTreeExpression> children;

    public MethodCallExpression(String methodName, CallTreeExpression... children) {
      super();
      this.methodName = methodName;
      this.children = Arrays.asList(children);
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      if (children.size() == 0) {
        return builder.callOnThisNoParam(methodName);
      } else {
        final Iterator<CallTreeExpression> it = children.iterator();
        ParamValueExpressionBuilder<X, EB, VEB> veb = it.next().compileToExpression(builder.callOnThis(methodName));
        while (it.hasNext()) {
          veb = it.next().compileToExpression(veb.nextParam());
        }
        return veb.endCall();
      }
    }

    @Override
    public String toString() {
      String s = methodName + "(";
      boolean first = true;
      for (CallTreeExpression child : children) {
        if (first) {
          first = false;
        } else {
          s += ", ";
        }
        s += child.toString();
      }
      s += ")";
      return s;
    }
  }

  public static class BooleanLiteralExpression extends CallTreeExpression {
    private boolean value;

    public BooleanLiteralExpression(boolean value) {
      this.value = value;
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      return builder.literal(value);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class IntLiteralExpression extends CallTreeExpression {
    private int value;

    public IntLiteralExpression(int value) {
      this.value = value;
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      return builder.literal(value);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class LongLiteralExpression extends CallTreeExpression {
    private long value;

    public LongLiteralExpression(long value) {
      this.value = value;
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      return builder.literal(value);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class FloatLiteralExpression extends CallTreeExpression {
    private float value;

    public FloatLiteralExpression(float value) {
      this.value = value;
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      return builder.literal(value);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class DoubleLiteralExpression extends CallTreeExpression {
    private double value;

    public DoubleLiteralExpression(double value) {
      this.value = value;
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      return builder.literal(value);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class StringLiteralExpression extends CallTreeExpression {
    private String value;

    public StringLiteralExpression(String value) {
      this.value = value;
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      return builder.literal(value);
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  public static class ParamExpression extends CallTreeExpression {
    private String name;

    public ParamExpression(String name) {
      this.name = name;
    }

    public <X, EB extends ExpressionBuilder<X, EB, VEB>, VEB extends ValueExpressionBuilder<X, EB, VEB>> VEB compileToExpression(final EB builder) {
      return builder.get(name);
    }

    @Override
    public String toString() {
      return name;
    }
  }

}
