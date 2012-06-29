package brennus.model;

abstract public class TypeConversion {

  public static final TypeConversion NONE = new NoTypeConversion();

  private static class NoTypeConversion extends TypeConversion {
    @Override
    public void accept(TypeConversionVisitor visitor) {
    }
  }

  abstract public void accept(TypeConversionVisitor visitor);

}
