package brennus.model;

public enum UnaryOperator implements Operator {

  NOT("!"),
  ISNULL("null =="),
  ISNOTNULL("null !="),
  ARRAYSIZE("size of array ");

  private final String representation;

  private UnaryOperator(String representation) {
    this.representation = representation;
  }

  @Override
  public String getRepresentation() {
    return representation;
  }
}
