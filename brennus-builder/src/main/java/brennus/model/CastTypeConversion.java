package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class CastTypeConversion extends TypeConversion {

  private final Type type;

  public CastTypeConversion(Type type) {
    this.type = type;
  }

  @Override
  public void accept(TypeConversionVisitor visitor) {
    wrap(visitor).visit(this);
  }

  public Type getType() {
    return type;
  }

}
