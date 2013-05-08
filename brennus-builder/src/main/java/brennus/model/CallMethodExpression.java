package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public final class CallMethodExpression extends Expression {

  private final Expression callee;
  private final String methodName;
  private final List<Expression> parameters;

  public CallMethodExpression(Expression callee, String methodName, List<Expression> parameters) {
    this.callee = callee;
    this.methodName = methodName;
    this.parameters = unmodifiableList(new ArrayList<Expression>(parameters));
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
    return "["+getClass().getSimpleName()+" "+(callee==null ? "this": callee)+"."+methodName+"("+parameters+")]";
  }

  public Expression getCallee() {
    return callee;
  }

}
