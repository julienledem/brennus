package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

import java.util.Collections;
import java.util.List;

public class SwitchStatement extends Statement {

  private final List<CaseStatement> caseStatements;
  private final CaseStatement defaultCaseStatement;

  public SwitchStatement(Expression expression, List<CaseStatement> caseStatements, CaseStatement defaultCaseStatement) {
    super(expression);
    this.caseStatements = Collections.unmodifiableList(caseStatements);
    this.defaultCaseStatement = defaultCaseStatement;

  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public List<CaseStatement> getCaseStatements() {
    return caseStatements;
  }

  public CaseStatement getDefaultCaseStatement() {
    return defaultCaseStatement;
  }

}
