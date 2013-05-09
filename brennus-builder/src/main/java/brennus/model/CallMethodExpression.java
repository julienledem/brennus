package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import brennus.ImmutableList;

public final class CallMethodExpression extends Expression {

  private final Expression callee;
  private final String methodName;
  private final ImmutableList<Expression> parameters;

  public CallMethodExpression(Expression callee, String methodName, ImmutableList<Expression> parameters) {
    this.callee = callee;
    this.methodName = methodName;
    this.parameters = parameters;
  }

  public String getMethodName() {
    return methodName;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public ImmutableList<Expression> getParameters() {
    return parameters;
  }

  @Override
  public String toString() {
    return "["+getClass().getSimpleName()+" "+(callee==null ? "this": callee)+"."+methodName+"("+parameters+")]";
  }

  public Expression getCallee() {
    return callee;
  }

}
