package brennus;

abstract public class Type {

  public static final Type VOID = existing(Void.TYPE);
  public static final Type INT = existing(Integer.TYPE);
  public static final Type LONG = existing(Long.TYPE);
  public static final Type STRING = existing(String.class);
  public static final Type OBJECT = existing(Object.class);

  public static Type existing(Class<?> existing) {
    return new ExistingType(existing);
  }

  abstract public void visit(TypeVisitor typeVisitor);

  abstract public String getName();

}
