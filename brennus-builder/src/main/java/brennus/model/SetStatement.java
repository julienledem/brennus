package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;


public final class SetStatement extends Statement {

  private final String to;

  public SetStatement(int line, String to, Expression expression) {
    super(expression, line);
    this.to = to;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
      wrap(statementVisitor).visit(this);
  }

  public String getTo() {
    return to;
  }

}
