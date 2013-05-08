package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public final class CaseBlockStatement extends CaseStatement {

  private final List<Statement> statements;
  private final boolean breakCase;

  public CaseBlockStatement(int line, LiteralExpression value, List<Statement> statements, boolean breakCase) {
    super(value, line);
    this.statements = unmodifiableList(new ArrayList<Statement>(statements));
    this.breakCase = breakCase;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public List<Statement> getStatements() {
    return statements;
  }

  public boolean isBreakCase() {
    return breakCase;
  }

  public LiteralExpression getliteralExpression() {
    return (LiteralExpression)getExpression();
  }

  @Override
  public void accept(CaseStatementVisitor visitor) {
    wrap(visitor).visit(this);
  }

}
