package brennus.model;

public class ReturnStatement extends Statement {

  public ReturnStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

}
