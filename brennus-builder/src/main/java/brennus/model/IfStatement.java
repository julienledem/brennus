package brennus.model;
import static brennus.model.ExceptionHandlingVisitor.wrap;
import brennus.ImmutableList;

public final class IfStatement extends Statement {

  private final ImmutableList<Statement> thenStatements;
  private final ImmutableList<Statement> elseStatements;

  public IfStatement(Expression ifExpression, int line, ImmutableList<Statement> thenStatements, ImmutableList<Statement> elseStatements) {
    super(ifExpression, line);
    this.thenStatements = thenStatements;
    this.elseStatements = elseStatements;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public ImmutableList<Statement> getThenStatements() {
    return thenStatements;
  }

  public ImmutableList<Statement> getElseStatements() {
    return elseStatements;
  }

}
