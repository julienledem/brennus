package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;


public class ReturnStatement extends Statement {

  public ReturnStatement(Expression expression) {
    super(expression);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

}
