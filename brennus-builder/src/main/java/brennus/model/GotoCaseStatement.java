package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;


public final class GotoCaseStatement extends CaseStatement {

  private final String label;

  public GotoCaseStatement(int line, int value, String label) {
    super(new LiteralExpression(value), line);
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  @Override
  public void accept(CaseStatementVisitor visitor) {
    wrap(visitor).visit(this);
  }

}
