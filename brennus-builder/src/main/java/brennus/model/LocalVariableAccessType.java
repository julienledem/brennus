package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class LocalVariableAccessType extends VarAccessType {

  private final String varName;
  private final int varIndex;
  private final Type type;

  public LocalVariableAccessType(String varName, int varIndex, Type type) {
    this.varName = varName;
    this.varIndex = varIndex;
    this.type = type;
  }

  @Override
  public void accept(VarAccessTypeVisitor visitor) {
    wrap(visitor).visit(this);
  }

  public String getVarName() {
    return varName;
  }

  public int getVarIndex() {
    return varIndex;
  }

  public Type getType() {
    return type;
  }

}
