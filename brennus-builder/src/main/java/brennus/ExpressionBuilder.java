package brennus;

import brennus.model.CallMethodExpression;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.GetExpression;
import brennus.model.ReturnStatement;
import brennus.model.Statement;
import brennus.model.ThrowStatement;

/**
 * builds an expression
 * TODO: revisit
 * @author Julien Le Dem
 *
 * @param <T> the type of the parent to return on completion
 */
public class ExpressionBuilder<T extends CodeBlockBuilder<? super Statement>> {

  private Expression expression;

  private final T parent;

  public ExpressionBuilder(T parent) {
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

  public SwitchBuilder<T> switchOn() {
    return new SwitchBuilder<T>(parent, getExpression());
  }

  public T throwException() {
    parent.addStatement(new ThrowStatement(getExpression()));
    return parent;
  }

}
