package brennus;

public class CallMethodExpression extends Expression {

  private final String methodName;

  public CallMethodExpression(String methodName) {
    this.methodName = methodName;
  }

  public String getMethodName() {
    return methodName;
  }

  @Override
  public void visit(ExpressionVisitor expressionVisitor) {
    expressionVisitor.visit(this);
  }

}
