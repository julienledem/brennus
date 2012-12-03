package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class CallConstructorStatement extends Statement {

  public CallConstructorStatement(int line, Expression expression) {
    super(expression, line);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }
}
