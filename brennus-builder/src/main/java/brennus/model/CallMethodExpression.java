package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

import java.util.List;

public class CallMethodExpression extends Expression {

  private final Expression callee;
  private final String methodName;
  private final List<Expression> parameters;

  public CallMethodExpression(Expression callee, String methodName, List<Expression> parameters) {
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

  public List<Expression> getParameters() {
    return parameters;
  }

  @Override
  public String toString() {
    return "["+getClass().getSimpleName()+" "+getCallee()+"."+methodName+"("+parameters+")]";
  }

  public Expression getCallee() {
    return callee;
  }

}
