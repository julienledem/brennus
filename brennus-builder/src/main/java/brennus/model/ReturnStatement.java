package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;


public final class ReturnStatement extends Statement {

  public ReturnStatement(int line, Expression expression) {
    super(expression, line);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

}
