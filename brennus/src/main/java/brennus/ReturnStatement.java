package brennus;

public class ReturnStatement extends Statement {

  public ReturnStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void visit(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

}
