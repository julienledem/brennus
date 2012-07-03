package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class ExpressionStatement extends Statement {

  public ExpressionStatement(int line, Expression expression) {
    super(expression, line);
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

}
