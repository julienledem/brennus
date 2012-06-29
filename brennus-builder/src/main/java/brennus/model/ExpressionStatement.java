package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class ExpressionStatement extends Statement {

  public ExpressionStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

}
