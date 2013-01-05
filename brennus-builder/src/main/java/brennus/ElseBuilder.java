package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.ThenBuilder.IfStatementHandler;
import brennus.model.Expression;
import brennus.model.IfStatement;
import brennus.model.Statement;

public class ElseBuilder<T> extends StatementBuilder<ElseBuilder<T>> {

  private final Expression ifExpression;
  private final int sourceLineNumber;
  private final IfStatementHandler<T> ifStatementHandler;
  private final List<Statement> thenStatements;
  private final List<Statement> elseStatements = new ArrayList<Statement>();

  ElseBuilder(Expression ifExpression, int sourceLineNumber, IfStatementHandler<T> ifStatementHandler, List<Statement> thenStatements) {
    this.ifExpression = ifExpression;
    this.sourceLineNumber = sourceLineNumber;
    this.ifStatementHandler = ifStatementHandler;
    this.thenStatements = thenStatements;
  }

  @Override
  protected StatementHandler<ElseBuilder<T>> statementHandler() {
    return new StatementHandler<ElseBuilder<T>>() {
      public ElseBuilder<T> handleStatement(Statement statement) {
        elseStatements.add(statement);
        return ElseBuilder.this;
      }
    };
  }

  public T endIf() {
    return ifStatementHandler.handleStatement(new IfStatement(ifExpression, sourceLineNumber, thenStatements, elseStatements));
  }

  public <S> S transform(Function<ElseBuilder<T>, S> function) {
    return function.apply(this);
  }
}
