package brennus;

import brennus.ThenBuilder.IfStatementHandler;
import brennus.model.Expression;
import brennus.model.IfStatement;
import brennus.model.Statement;

public final class ElseBuilder<T> extends StatementBuilder<ElseBuilder<T>> {

  private final Expression ifExpression;
  private final int sourceLineNumber;
  private final IfStatementHandler<T> ifStatementHandler;
  private final ImmutableList<Statement> thenStatements;
  private final ImmutableList<Statement> elseStatements;

  ElseBuilder(Expression ifExpression, int sourceLineNumber, IfStatementHandler<T> ifStatementHandler, ImmutableList<Statement> thenStatements, Builder builder) {
    this(
        builder,
        ifExpression,
        sourceLineNumber,
        ifStatementHandler,
        thenStatements,
        ImmutableList.<Statement>empty());
  }

  private ElseBuilder(
      Builder builder,
      Expression ifExpression,
      int sourceLineNumber,
      IfStatementHandler<T> ifStatementHandler,
      ImmutableList<Statement> thenStatements,
      ImmutableList<Statement> elseStatements) {
    super(builder);
    this.ifExpression = ifExpression;
    this.sourceLineNumber = sourceLineNumber;
    this.ifStatementHandler = ifStatementHandler;
    this.thenStatements = thenStatements;
    this.elseStatements = elseStatements;
  }

  @Override
  protected StatementHandler<ElseBuilder<T>> statementHandler() {
    return new StatementHandler<ElseBuilder<T>>() {
      public ElseBuilder<T> handleStatement(Statement statement) {
        return new ElseBuilder<T>(
            builder,
            ifExpression,
            sourceLineNumber,
            ifStatementHandler,
            thenStatements,
            elseStatements.append(statement));
      }
    };
  }

  public T endIf() {
    return ifStatementHandler.handleStatement(new IfStatement(ifExpression, sourceLineNumber, thenStatements, elseStatements));
  }

  public <S> S map(Function<ElseBuilder<T>, S> function) {
    return function.apply(this);
  }
}
