package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class ThrowStatement extends Statement {

  public ThrowStatement(int line, Expression expression) {
    super(expression, line);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }
}
