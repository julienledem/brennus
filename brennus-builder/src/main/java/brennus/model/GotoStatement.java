package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class GotoStatement extends Statement {

  private final String name;

  public GotoStatement(int line, String name) {
    super(null, line);
    this.name = name;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public String getName() {
    return name;
  }

}
