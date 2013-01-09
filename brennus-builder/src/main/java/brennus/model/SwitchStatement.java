package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

import java.util.Collections;
import java.util.List;

public class SwitchStatement extends Statement {

  private final List< CaseStatement> caseStatements;
  private final CaseBlockStatement defaultCaseStatement;

  public SwitchStatement(int line, Expression expression, List<CaseStatement> caseStatements, CaseBlockStatement defaultCaseStatement) {
    super(expression, line);
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

  public CaseBlockStatement getDefaultCaseStatement() {
    return defaultCaseStatement;
  }

}
