package brennus.model;

public final class Parameter {

  private final Type type;
  private final String name;
  private final int index;

  public Parameter(Type type, String name, int index) {
    this.type = type;
    this.name = name;
    this.index = index;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public String toString() {
   return "Parameter{" + type + " " + name + " " + index + "}";
  }
}
