package brennus;

import brennus.CaseBuilder.CaseStatementHandler;
import brennus.model.CaseBlockStatement;
import brennus.model.CaseStatement;
import brennus.model.Expression;
import brennus.model.GotoCaseStatement;
import brennus.model.LiteralExpression;
import brennus.model.SwitchStatement;


/**
 * Builds a switch block
 *
 * @author Julien Le Dem
 *
 * @param <T> the type of the parent to return on completion
 */
public final class SwitchBuilder<T> {

  interface SwitchStatementsHandler<T> {
    T handleStatement(SwitchStatement switchStatement);
  }

  private final Expression switchOnExpression;
  private final int line;
  private final SwitchStatementsHandler<T> switchStatementHandler;
  private final ImmutableList<CaseStatement> statements;
  private final CaseBlockStatement defaultCaseStatement;
  private final Builder builder;

  SwitchBuilder(Expression switchOnExpression, SwitchStatementsHandler<T> switchStatementHandler, Builder builder) {
    this(
        switchOnExpression,
        builder.getSourceLineNumber(),
        switchStatementHandler,
        ImmutableList.<CaseStatement>empty(),
        null,
        builder);
  }

  private SwitchBuilder(Expression switchOnExpression, int line,
      SwitchStatementsHandler<T> switchStatementHandler,
      ImmutableList<CaseStatement> statements,
      CaseBlockStatement defaultCaseStatement, Builder builder) {
    super();
    this.switchOnExpression = switchOnExpression;
    this.line = line;
    this.switchStatementHandler = switchStatementHandler;
    this.statements = statements;
    this.defaultCaseStatement = defaultCaseStatement;
    this.builder = builder;
  }

  private SwitchBuilder<T> newSwitchBuilder(
      ImmutableList<CaseStatement> newStatements,
      CaseBlockStatement newDefaultCaseStatement) {
    return new SwitchBuilder<T>(
        switchOnExpression,
        line,
        switchStatementHandler,
        newStatements,
        newDefaultCaseStatement,
        builder);
  }

  public CaseBuilder<T> caseBlock(int value) {
    return new CaseBuilder<T>(new LiteralExpression(value), new CaseStatementHandler<T>() {
      public SwitchBuilder<T> handleStatement(CaseBlockStatement statement) {
        return newSwitchBuilder(
            statements.append(statement),
            defaultCaseStatement);
      }
    }, builder);
  }

  public SwitchBuilder<T> gotoLabel(int value, String label) {
    return newSwitchBuilder(
        statements.append(new GotoCaseStatement(builder.getSourceLineNumber(), value, label)),
        defaultCaseStatement);
  }

  public CaseBuilder<T> defaultCase() {
    if (defaultCaseStatement != null) {
      throw new RuntimeException("already a default case "+defaultCaseStatement);
    }
    return new CaseBuilder<T>(null, new CaseStatementHandler<T>() {
      public SwitchBuilder<T> handleStatement(CaseBlockStatement statement) {
        return newSwitchBuilder(
            statements,
            statement);
      }
    }, builder);
  }

  public T endSwitch() {
    return switchStatementHandler.handleStatement(new SwitchStatement(line, switchOnExpression, statements, defaultCaseStatement));
  }

  public <U> U map(Function<SwitchBuilder<T>, U> function) {
    return function.apply(this);
  }

}
