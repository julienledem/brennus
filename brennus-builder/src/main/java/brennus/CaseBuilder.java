package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.model.CaseStatement;
import brennus.model.LiteralExpression;
import brennus.model.Statement;

/**
 * builds a case
 *
 * @author Julien Le Dem
 *
 * @param <T> the type of the parent of the switch to return on completion
 */
public class CaseBuilder<T> extends StatementBuilder<CaseBuilder<T>> {

  public interface CaseStatementHandler<T> {
    SwitchBuilder<T> handleStatement(CaseStatement caseStatement);
  }

  private final LiteralExpression literalExpression;
  private final CaseStatementHandler<T> statementHandler;
  private final List<Statement> statements = new ArrayList<Statement>();

  CaseBuilder(LiteralExpression literalExpression, CaseStatementHandler<T> statementHandler) {
    this.literalExpression = literalExpression;
    this.statementHandler = statementHandler;
  }

  public SwitchBuilder<T> endCase() {
    return statementHandler.handleStatement(new CaseStatement(literalExpression, statements));
  }

  protected StatementHandler<CaseBuilder<T>> statementHandler() {
    return new StatementHandler<CaseBuilder<T>>() {
      public CaseBuilder<T> handleStatement(Statement statement) {
        statements.add(statement);
        return CaseBuilder.this;
      }
    };
  }

}
