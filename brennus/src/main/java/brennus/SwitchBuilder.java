package brennus;



public class SwitchBuilder
// the parent of this class can contain SwitchStatements
<P extends CodeBlockBuilder<? super Statement>>
// This class is a code block accepting CaseStatements
extends CodeBlockBuilder<CaseStatement> {

  private final P parent;
  private final Expression switchOnExpression;

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
    parent.addStatement(new SwitchStatement(switchOnExpression, getStatements()));
    return parent;
  }

}
