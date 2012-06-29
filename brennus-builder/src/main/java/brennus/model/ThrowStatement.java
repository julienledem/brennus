package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class ThrowStatement extends Statement {

  public ThrowStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }
}
