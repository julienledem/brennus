package brennus;

import brennus.model.Expression;
import brennus.model.IfStatement;
import brennus.model.Statement;

public final class ThenBuilder<T> extends StatementBuilder<ThenBuilder<T>> {

  static interface IfStatementHandler<T> {
    T handleStatement(IfStatement ifStatement);
  }

  private final Expression e;
  private final IfStatementHandler<T> ifStatementHandler;
  private final int sourceLineNumber;
  private final ImmutableList<Statement> thenStatements;

  ThenBuilder(Expression e, IfStatementHandler<T> ifStatementHandler, Builder builder) {
    this(
        builder,
        e,
        ifStatementHandler,
        builder.getSourceLineNumber(),
        ImmutableList.<Statement>empty());
  }

  private ThenBuilder(
      Builder builder,
      Expression e,
      IfStatementHandler<T> ifStatementHandler,
      int sourceLineNumber,
      ImmutableList<Statement> thenStatements) {
    super(builder);
    this.e = e;
    this.ifStatementHandler = ifStatementHandler;
    this.sourceLineNumber = sourceLineNumber;
    this.thenStatements = thenStatements;
  }



  @Override
  protected StatementHandler<ThenBuilder<T>> statementHandler() {
    return new StatementHandler<ThenBuilder<T>>() {
      public ThenBuilder<T> handleStatement(Statement statement) {
        return new ThenBuilder<T>(
            builder,
            e,
            ifStatementHandler,
            sourceLineNumber,
            thenStatements.append(statement));
      }
    };
  }

  public T endIf() {
    return elseBlock().endIf();
  }

  public ElseBuilder<T> elseBlock() {
    return new ElseBuilder<T>(e, sourceLineNumber, ifStatementHandler, thenStatements, builder);
  }

  public <S> S map(Function<ThenBuilder<T>, S> function) {
    return function.apply(this);
  }

}
