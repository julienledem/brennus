package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import brennus.ImmutableList;

public final class SwitchStatement extends Statement {

  private final ImmutableList<CaseStatement> caseStatements;
  private final CaseBlockStatement defaultCaseStatement;

  public SwitchStatement(int line, Expression expression, ImmutableList<CaseStatement> caseStatements, CaseBlockStatement defaultCaseStatement) {
    super(expression, line);
    this.caseStatements = caseStatements;
    this.defaultCaseStatement = defaultCaseStatement;

  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public ImmutableList<CaseStatement> getCaseStatements() {
    return caseStatements;
  }

  public CaseBlockStatement getDefaultCaseStatement() {
    return defaultCaseStatement;
  }

}
