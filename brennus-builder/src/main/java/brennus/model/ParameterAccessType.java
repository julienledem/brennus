package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class ParameterAccessType extends VarAccessType {

  private final Parameter param;

  public ParameterAccessType(Parameter param) {
    this.param = param;
  }

  public Parameter getParam() {
    return param;
  }

  @Override
  public void accept(VarAccessTypeVisitor visitor) {
    wrap(visitor).visit(this);
  }

}
