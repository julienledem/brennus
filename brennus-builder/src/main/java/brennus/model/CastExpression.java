package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class CastExpression extends Expression {

  private final Type type;
  private final Expression expression;

  public CastExpression(Type type, Expression expression) {
    this.type = type;
    this.expression = expression;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  public Type getType() {
    return type;
  }

  public Expression getExpression() {
    return expression;
  }

}
