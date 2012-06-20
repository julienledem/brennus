package brennus;

public class ExpressionStatement extends Statement {

  public ExpressionStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void visit(StatementVisitor statementVisitor) {
    statementVisitor.visit(this);
  }

}
