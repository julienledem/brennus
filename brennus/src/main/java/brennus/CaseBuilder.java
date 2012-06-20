package brennus;

public class CaseBuilder<T extends CodeBlockBuilder<? super Statement>> extends CodeBlockBuilder<Statement> {

  private final Expression value;
  private final SwitchBuilder<T> switchBuilder;

  public CaseBuilder(Expression value, SwitchBuilder<T> switchBuilder) {
    this.value = value;
    this.switchBuilder = switchBuilder;
  }

  public CaseBuilder(SwitchBuilder<T> switchBuilder) {
    this.value = null;
    this.switchBuilder = switchBuilder;
  }

  public SwitchBuilder<T> endCase() {
    switchBuilder.addStatement(new CaseStatement(value, getStatements()));
    return switchBuilder;
  }

  public ExpressionBuilder<CaseBuilder<T>> expression() {
    return new ExpressionBuilder<CaseBuilder<T>>(this);
  }

}
