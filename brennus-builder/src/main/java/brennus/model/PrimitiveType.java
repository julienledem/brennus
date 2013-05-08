package brennus.model;

public final class PrimitiveType extends ExistingType {

  private final Type boxedType;

  PrimitiveType(Class<?> existing, String identifier, String signature, Type boxedType) {
    super(existing, identifier, signature);
    this.boxedType = boxedType;
  }

  public Type getBoxedType() {
    return boxedType;
  }

}
