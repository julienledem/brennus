package brennus;

import brennus.model.CaseStatement;
import brennus.model.Expression;
import brennus.model.LiteralExpression;
import brennus.model.Statement;
import brennus.model.SwitchStatement;


/**
 * Builds a switch block
 *
 * @author Julien Le Dem
 *
 * @param <P> the type of the parent to return on completion
 */
public class SwitchBuilder
// the parent of this class can contain SwitchStatements
<P extends CodeBlockBuilder<? super Statement>>
// This class is a code block accepting CaseStatements
extends CodeBlockBuilder<CaseStatement> {

  private final P parent;
  private final Expression switchOnExpression;
  private CaseStatement defaultCaseStatement;

  public SwitchBuilder(P parent, Expression switchOnExpression) {
    this.parent = parent;
    this.switchOnExpression = switchOnExpression;
  }

  public CaseBuilder<P> caseBlock(int value) {
    return new CaseBuilder<P>(new LiteralExpression(value), this);
  }

  public CaseBuilder<P> defaultCase() {
    return new CaseBuilder<P>(this);
  }

  public P endSwitch() {
    parent.addStatement(new SwitchStatement(switchOnExpression, getStatements(), defaultCaseStatement));
    return parent;
  }

  @Override
  void addStatement(CaseStatement statement) {
    if (statement.getExpression() == null) {
      if (defaultCaseStatement != null) {
        throw new RuntimeException("multiple default cases "+defaultCaseStatement+" and "+statement);
      }
      this.defaultCaseStatement = statement;
    } else {
      super.addStatement(statement);
    }
  }


}
