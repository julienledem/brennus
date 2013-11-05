package brennus.eval;

import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import brennus.Builder;
import brennus.ExpressionBuilder;
import brennus.Function;
import brennus.MethodBuilder;
import brennus.MethodDeclarationBuilder;
import brennus.ParamValueExpressionBuilder;
import brennus.ReturnExpressionBuilder;
import brennus.ReturnValueExpressionBuilder;
import brennus.ValueExpressionBuilder;
import brennus.model.FutureType;
import brennus.model.Type;

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

  private static int id = 0;

  abstract <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder);

  public FutureType compile(Class<?> parent, Type returnType, String methodName, final Class<?>... parameters) {
    FutureType c = new Builder()
      .startClass("brennus.eval.CallTreeExpression$Expression" + (++id), existing(parent))
      .startMethod(PUBLIC, returnType, methodName)
        .transform(new Function<MethodDeclarationBuilder, MethodDeclarationBuilder>() {
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
            .transform(new Function<ReturnExpressionBuilder<MethodBuilder>, ReturnValueExpressionBuilder<MethodBuilder>>() {
              @Override
              public ReturnValueExpressionBuilder<MethodBuilder> apply(ReturnExpressionBuilder<MethodBuilder> input) {
                return compileTo(input);
              }
            })
          .endReturn()
        .endMethod()
      .endClass();
    return c;

  }

  public static class MethodCallExpression extends CallTreeExpression {
    private final String methodName;
    private final List<CallTreeExpression> children;

    public MethodCallExpression(String methodName, CallTreeExpression... children) {
      super();
      this.methodName = methodName;
      this.children = Arrays.asList(children);
    }

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
      if (children.size() == 0) {
        return builder.callOnThisNoParam(methodName);
      } else {
        final Iterator<CallTreeExpression> it = children.iterator();
        ParamValueExpressionBuilder<T, EB, VEB> veb = it.next().compileTo(builder.callOnThis(methodName));
        while (it.hasNext()) {
          veb = it.next().compileTo(veb.nextParam());
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

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
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

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
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

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
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

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
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

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
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

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
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

    public <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB compileTo(final ExpressionBuilder<T, EB, VEB> builder) {
      return builder.get(name);
    }

    @Override
    public String toString() {
      return name;
    }
  }

}
