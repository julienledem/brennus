package brennus;

import brennus.model.Type;

public class LocalVarContext {

  private final String name;
  private final int index;
  private final Type type;

  public LocalVarContext(String name, int index, Type type) {
    this.name = name;
    this.index = index;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public int getIndex() {
    return index;
  }

  public Type getType() {
    return type;
  }

}
