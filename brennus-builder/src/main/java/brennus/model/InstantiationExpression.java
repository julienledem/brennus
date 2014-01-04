package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import brennus.ImmutableList;

public class InstantiationExpression extends Expression {

  private final Type type;
  private final ImmutableList<Expression> parameters;

  public InstantiationExpression(Type type, ImmutableList<Expression> parameters) {
    this.type = type;
    this.parameters = parameters;
  }

  public Type getType() {
    return type;
  }

  public ImmutableList<Expression> getParameters() {
    return parameters;
  }

  @Override
  public void accept(ExpressionVisitor expressionVisitor) {
    wrap(expressionVisitor).visit(this);
  }

  @Override
  public String toString() {
    return "[new " + type.getName() + "()]";
  }
}
