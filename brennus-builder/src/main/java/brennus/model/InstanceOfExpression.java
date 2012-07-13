package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class InstanceOfExpression extends Expression {

  private final Expression expression;
  private final Type type;

  public InstanceOfExpression(Expression expression, Type type) {
    this.expression = expression;
    this.type = type;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public Expression getExpression() {
    return expression;
  }

  public Type getType() {
    return type;
  }

}
