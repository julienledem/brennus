package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class DefineVarStatement extends Statement {

  private final String varName;
  private final Type type;

  public DefineVarStatement(int sourceLineNumber, Type type, String varName) {
    super(null, sourceLineNumber);
    this.type = type;
    this.varName = varName;
  }

  @Override
  public void accept(StatementVisitor statementVisitor) {
    wrap(statementVisitor).visit(this);
  }

  public String getVarName() {
    return varName;
  }

  public Type getType() {
    return type;
  }

}
