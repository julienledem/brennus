package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.CaseBuilder.CaseStatementHandler;
import brennus.model.CaseStatement;
import brennus.model.CaseBlockStatement;
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
public class SwitchBuilder<T> {

  interface SwitchStatementsHandler<T> {
    T handleStatement(SwitchStatement switchStatement);
  }

  private final Expression switchOnExpression;
  private final int line;
  private final SwitchStatementsHandler<T> switchStatementHandler;
  private final List<CaseStatement> statements = new ArrayList<CaseStatement>();
  private CaseBlockStatement defaultCaseStatement;
  private final Builder builder;

  SwitchBuilder(Expression switchOnExpression, SwitchStatementsHandler<T> switchStatementHandler, Builder builder) {
    this.switchOnExpression = switchOnExpression;
    this.switchStatementHandler = switchStatementHandler;
    this.builder = builder;
    this.line = builder.getSourceLineNumber();
  }

  public CaseBuilder<T> caseBlock(int value) {
    return new CaseBuilder<T>(new LiteralExpression(value), new CaseStatementHandler<T>() {
      public SwitchBuilder<T> handleStatement(CaseBlockStatement statement) {
        statements.add(statement);
        return SwitchBuilder.this;
      }
    }, builder);
  }

  public SwitchBuilder<T> gotoLabel(int value, String label) {
    statements.add(new GotoCaseStatement(builder.getSourceLineNumber(), value, label));
    return this;
  }

  public CaseBuilder<T> defaultCase() {
    if (defaultCaseStatement != null) {
      throw new RuntimeException("already a default case "+defaultCaseStatement);
    }
    return new CaseBuilder<T>(null, new CaseStatementHandler<T>() {
      public SwitchBuilder<T> handleStatement(CaseBlockStatement statement) {
        defaultCaseStatement = statement;
        return SwitchBuilder.this;
      }
    }, builder);
  }

  public T endSwitch() {
    return switchStatementHandler.handleStatement(new SwitchStatement(line, switchOnExpression, statements, defaultCaseStatement));
  }

  public <U> U transform(Function<SwitchBuilder<T>, U> function) {
    return function.apply(this);
  }

}
