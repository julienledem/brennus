package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public class UnboxingTypeConversion extends TypeConversion {

  private final PrimitiveType primitiveType;

  public UnboxingTypeConversion(PrimitiveType primitiveType) {
    super();
    this.primitiveType = primitiveType;
  }

  public PrimitiveType getPrimitiveType() {
    return primitiveType;
  }

  @Override
  public void accept(TypeConversionVisitor visitor) {
    wrap(visitor).visit(this);
  }

  @Override
  public String toString() {
    return "["+this.getClass().getSimpleName()+" "+primitiveType+"]";
  }
}
