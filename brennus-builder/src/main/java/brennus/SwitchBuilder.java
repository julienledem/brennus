package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.CaseBuilder.CaseStatementHandler;
import brennus.model.CaseStatement;
import brennus.model.Expression;
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
  private CaseStatement defaultCaseStatement;

  SwitchBuilder(Expression switchOnExpression, SwitchStatementsHandler<T> switchStatementHandler) {
    this.switchOnExpression = switchOnExpression;
    this.switchStatementHandler = switchStatementHandler;
    this.line = MethodContext.getSourceLineNumber();
  }

  public CaseBuilder<T> caseBlock(int value) {
    return new CaseBuilder<T>(new LiteralExpression(value), new CaseStatementHandler<T>() {
      public SwitchBuilder<T> handleStatement(CaseStatement statement) {
        statements.add(statement);
        return SwitchBuilder.this;
      }
    });
  }

  public CaseBuilder<T> defaultCase() {
    if (defaultCaseStatement != null) {
      throw new RuntimeException("already a default case "+defaultCaseStatement);
    }
    return new CaseBuilder<T>(null, new CaseStatementHandler<T>() {
      public SwitchBuilder<T> handleStatement(CaseStatement statement) {
        defaultCaseStatement = statement;
        return SwitchBuilder.this;
      }
    });
  }

  public T endSwitch() {
    return switchStatementHandler.handleStatement(new SwitchStatement(line, switchOnExpression, statements, defaultCaseStatement));
  }

}
