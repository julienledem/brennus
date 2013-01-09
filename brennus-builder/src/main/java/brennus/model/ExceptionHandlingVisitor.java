package brennus.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class ExceptionHandlingVisitor {

  static TypeVisitor wrap(final TypeVisitor typeVisitor) {
    return wrap(typeVisitor, TypeVisitor.class);
  }

  static StatementVisitor wrap(StatementVisitor statementVisitor) {
    return wrap(statementVisitor, StatementVisitor.class);
  }

  static ExpressionVisitor wrap(final ExpressionVisitor expressionVisitor) {
    return wrap(expressionVisitor, ExpressionVisitor.class);
  }

  static TypeConversionVisitor wrap(final TypeConversionVisitor typeConversionVisitor) {
    return wrap(typeConversionVisitor, TypeConversionVisitor.class);
  }

  static VarAccessTypeVisitor wrap(final VarAccessTypeVisitor varAccessTypeVisitor) {
    return wrap(varAccessTypeVisitor, VarAccessTypeVisitor.class);
  }

  static CaseStatementVisitor wrap(CaseStatementVisitor visitor) {
    return wrap(visitor, CaseStatementVisitor.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T wrap(final T visitor, Class<T> interf) {
    return (T)Proxy.newProxyInstance(ExceptionHandlingVisitor.class.getClassLoader(), new Class<?>[] {interf}, new InvocationHandler() {
      public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable {
        try {
          return method.invoke(visitor, args);
        } catch (InvocationTargetException e) {
          throw new VisitorRuntimeException(method.getDeclaringClass().getSimpleName(), args[0], e.getTargetException());
        }
      }
    });
  }

}
