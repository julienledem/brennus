package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class CallMethodExpression extends Expression {

  private final String methodName;

  public CallMethodExpression(String methodName) {
    this.methodName = methodName;
  }

  public String getMethodName() {
    return methodName;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  @Override
  public String toString() {
    return "["+getClass().getSimpleName()+" "+methodName+"]";
  }

}
