package brennus.eval;

import static brennus.model.ExistingType.BOOLEAN;
import static brennus.model.ExistingType.DOUBLE;
import static brennus.model.ExistingType.FLOAT;
import static brennus.model.ExistingType.INT;
import static brennus.model.ExistingType.LONG;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import brennus.Builder;
import brennus.ExpressionBuilder;
import brennus.MethodBuilder;
import brennus.ReturnExpressionBuilder;
import brennus.ValueExpressionBuilder;
import brennus.asm.DynamicClassLoader;
import brennus.eval.DynamicExpression.BinaryExpression;
import brennus.eval.DynamicExpression.BinaryExpression.ArithmeticOperator;
import brennus.eval.DynamicExpression.BinaryExpression.BooleanOperator;
import brennus.eval.DynamicExpression.BinaryExpression.ComparisonOperator;
import brennus.eval.DynamicExpression.BinaryOperatorVisitor;
import brennus.eval.DynamicExpression.CastExpression;
import brennus.eval.DynamicExpression.ConstantExpression;
import brennus.eval.DynamicExpression.ExpressionVisitor;
import brennus.eval.DynamicExpression.ParamExpression;
import brennus.eval.DynamicExpression.Type;
import brennus.eval.DynamicExpression.UnaryExpression;
import brennus.eval.DynamicExpression.UnaryExpression.UnaryOperator;
import brennus.model.FutureType;
import brennus.model.PrimitiveType;

public class ExpressionCompiler {

  private DynamicClassLoader cl = new DynamicClassLoader();

  private static int id = 0;

  public Expression compileExpression(DynamicExpression e) {
    PrimitiveType type;
    switch (e.returnType) {
    case INT: type = INT; break;
    case LONG: type = LONG; break;
    case FLOAT: type = FLOAT; break;
    case DOUBLE: type = DOUBLE; break;
    case BOOLEAN: type = BOOLEAN; break;
    default: throw new UnsupportedOperationException(e.returnType.name());
    }
    final ReturnExpressionBuilder<MethodBuilder> b = new Builder()
    .startClass("brennus.eval.ExpressionCompiler$Expression" + (++id), existing(GeneratedExpression.class))
      .startMethod(PUBLIC, type, camelCase("eval", e.returnType.name())).param(existing(Parameters.class), "parameters")
        .returnExp();
    FutureType c = eval(b, e).endReturn().endMethod().endClass();
//    new TypePrinter().print(c);
    cl.define(c);
    try {
      return (Expression)cl.loadClass(c.getName()).newInstance();
    } catch (InstantiationException e1) {
      throw new RuntimeException(e1);
    } catch (IllegalAccessException e1) {
      throw new RuntimeException(e1);
    } catch (ClassNotFoundException e1) {
      throw new RuntimeException(e1);
    }
  }

  private <T, EB, VEB extends ValueExpressionBuilder<T, EB, VEB>> VEB eval(final ExpressionBuilder<T, EB, VEB> b, DynamicExpression e) {
     return e.accept(new ExpressionVisitor<VEB>() {
      @Override
      public VEB visit(final BinaryExpression e) {
        String methodName = e.operator.accept(new BinaryOperatorVisitor<String>() {
          @Override
          public String visit(ArithmeticOperator o) {
            return camelCase(o.name(), e.returnType.name());
          }
          @Override
          public String visit(ComparisonOperator o) {
            Type type = e.left.returnType;
            return camelCase(o.name(), type.name());
          }
          @Override
          public String visit(BooleanOperator o) {
            return camelCase(o.name());
          }
        });
        return eval(eval(b.callOnThis(methodName), e.left).nextParam(), e.right).endCall();
      }

      @Override
      public VEB visit(ParamExpression e) {
        return b.get("parameters").call(camelCase("get", e.returnType.name())).literal(e.index).endCall();
      }

      @Override
      public VEB visit(ConstantExpression e) {
        switch (e.returnType) {
        case INT: return b.literal((Integer)e.value);
        case LONG: return b.literal((Long)e.value);
        case FLOAT: return b.literal((Float)e.value);
        case DOUBLE: return b.literal((Double)e.value);
        case BOOLEAN: return b.literal((Boolean)e.value);
        default:
          throw new UnsupportedOperationException(e.returnType.name());
        }
      }

      @Override
      public VEB visit(UnaryExpression e) {
        String name;
        if (e.operator == UnaryOperator.MINUS) {
          name = camelCase(e.operator.name(), e.returnType.name());
        } else {
          name = camelCase(e.operator.name());
        }
        return eval(b.callOnThis(name), e.operand).endCall();
      }

      @Override
      public VEB visit(CastExpression e) {
        return eval(b.callOnThis(camelCase("cast", e.returnType.name(), e.castedExpression.returnType.name())), e.castedExpression).endCall();
      }
    });
  }

  public String camelCase(String... s) {
    StringBuilder sb = new StringBuilder();
    if (s.length > 0) {
      sb.append(s[0].toLowerCase());
      for (int i = 1; i < s.length; i++) {
        if (s[i].length() > 0) {
          sb.append(s[i].substring(0, 1).toUpperCase());
          sb.append(s[i].substring(1).toLowerCase());
        }
      }
    }
    return sb.toString();
  }
}