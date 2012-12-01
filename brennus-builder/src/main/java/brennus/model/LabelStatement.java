package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class LabelStatement extends Statement {

  private final String name;

  public LabelStatement(int line, String name) {
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
