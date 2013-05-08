package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public final class CallConstructorExpression extends Expression {
  private final List<Expression> parameters;

  public CallConstructorExpression(List<Expression> parameters) {
    this.parameters = unmodifiableList(new ArrayList<Expression>(parameters));
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
    return "["+getClass().getSimpleName()+" super("+parameters+")]";
  }

}
