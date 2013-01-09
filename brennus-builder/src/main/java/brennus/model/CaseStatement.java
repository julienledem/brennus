package brennus.model;

abstract public class CaseStatement extends Statement {

  public CaseStatement(LiteralExpression value, int line) {
    super(value, line);
  }

  abstract public void accept(CaseStatementVisitor visitor);

}
