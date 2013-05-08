package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public final class SwitchStatement extends Statement {

  private final List< CaseStatement> caseStatements;
  private final CaseBlockStatement defaultCaseStatement;

  public SwitchStatement(int line, Expression expression, List<CaseStatement> caseStatements, CaseBlockStatement defaultCaseStatement) {
    super(expression, line);
    this.caseStatements = unmodifiableList(new ArrayList<CaseStatement>(caseStatements));
    this.defaultCaseStatement = defaultCaseStatement;

  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public List<CaseStatement> getCaseStatements() {
    return caseStatements;
  }

  public CaseBlockStatement getDefaultCaseStatement() {
    return defaultCaseStatement;
  }

}
