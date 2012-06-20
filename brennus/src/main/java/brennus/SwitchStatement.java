package brennus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SwitchStatement extends Statement {

  private final Collection<CaseStatement> caseStatements;

  public SwitchStatement(Expression expression, List<CaseStatement> caseStatements) {
    super(expression);
    this.caseStatements = Collections.unmodifiableList(caseStatements);

  }

  @Override
  public void visit(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

  public Collection<CaseStatement> getCaseStatements() {
    return caseStatements;
  }

}
