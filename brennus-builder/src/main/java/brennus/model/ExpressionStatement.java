package brennus.model;

public class ExpressionStatement extends Statement {

  public ExpressionStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

}
