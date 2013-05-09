package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import brennus.ImmutableList;

public final class CallConstructorExpression extends Expression {
  private final ImmutableList<Expression> parameters;

  public CallConstructorExpression(ImmutableList<Expression> parameters) {
    this.parameters = parameters;
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
    return "["+getClass().getSimpleName()+" super("+parameters+")]";
  }

}
