package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.model.Expression;
import brennus.model.IfStatement;
import brennus.model.Statement;

public class ThenBuilder<T> extends StatementBuilder<ThenBuilder<T>> {

  static interface IfStatementHandler<T> {
    T handleStatement(IfStatement ifStatement);
  }

  private final Expression e;
  private final IfStatementHandler<T> ifStatementHandler;
  private final int sourceLineNumber;
  private final List<Statement> thenStatements = new ArrayList<Statement>();

  ThenBuilder(Expression e, IfStatementHandler<T> ifStatementHandler) {
    this.e = e;
    this.ifStatementHandler = ifStatementHandler;
    this.sourceLineNumber = MethodContext.getSourceLineNumber();
  }

  @Override
  protected StatementHandler<ThenBuilder<T>> statementHandler() {
    return new StatementHandler<ThenBuilder<T>>() {
      public ThenBuilder<T> handleStatement(Statement statement) {
        thenStatements.add(statement);
        return ThenBuilder.this;
      }
    };
  }

  public T endIf() {
    return elseBlock().endIf();
  }

  public ElseBuilder<T> elseBlock() {
    return new ElseBuilder<T>(e, sourceLineNumber, ifStatementHandler, thenStatements);
  }

}
