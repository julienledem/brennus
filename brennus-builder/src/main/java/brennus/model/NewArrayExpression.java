package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class NewArrayExpression extends Expression {

  private final Type type;
  private final Expression size;

  public NewArrayExpression(Type type, Expression size) {
    this.type = type;
    this.size = size;
  }

  public Type getType() {
    return type;
  }

  public Expression getSize() {
    return size;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

}
