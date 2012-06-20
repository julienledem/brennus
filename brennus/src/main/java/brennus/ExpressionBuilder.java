package brennus;

public class ExpressionBuilder<T extends CodeBlockBuilder<? super Statement>> {

  private Expression expression;

  private final T parent;

  ExpressionBuilder(T parent) {
    this.parent = parent;
  }

  Expression getExpression() {
    return expression;
  }

  public ExpressionBuilder<T> get(String name) {
    expression = new GetExpression(name);
    return this;
  }

  public T returnExp() {
    parent.addStatement(new ReturnStatement(getExpression()));
    return parent;
  }

  public ExpressionBuilder<T> call(String methodName) {
    expression = new CallMethodExpression(methodName);
    return this;
  }

  public T done() {
    parent.addStatement(new ExpressionStatement(getExpression()));
    return parent;
  }

  public SwitchBuilder<T> switchOnExp() {
    return new SwitchBuilder<T>(parent, getExpression());
  }

}
