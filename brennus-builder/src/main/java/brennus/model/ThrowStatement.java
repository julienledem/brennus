package brennus.model;

public class ThrowStatement extends Statement {

  public ThrowStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }
}
