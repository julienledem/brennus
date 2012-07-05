package brennus.model;
import static brennus.model.ExceptionHandlingVisitor.wrap;

import java.util.List;
public class IfStatement extends Statement {

  private final List<Statement> thenStatements;
  private final List<Statement> elseStatements;

  public IfStatement(Expression ifExpression, int line, List<Statement> thenStatements, List<Statement> elseStatements) {
    super(ifExpression, line);
    this.thenStatements = thenStatements;
    this.elseStatements = elseStatements;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public List<Statement> getThenStatements() {
    return thenStatements;
  }

  public List<Statement> getElseStatements() {
    return elseStatements;
  }

}
