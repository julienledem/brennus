package brennus.model;

public enum BinaryOperator implements Operator {
  PLUS("+"),
  EQUALS("=="),
  GREATER_THAN(">"),
  AND("&&"),
  GETARRAYATINDEX("[]");

  private final String representation;

  private BinaryOperator(String representation) {
    this.representation = representation;
  }

  @Override
  public String getRepresentation() {
    return representation;
  }
}
