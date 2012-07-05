package brennus.model;

public enum BinaryOperator {
  PLUS("+"),
  EQUALS("=="),
  GREATER_THAN(">");

  private final String representation;

  private BinaryOperator(String representation) {
    this.representation = representation;

  }

  public String getRepresentation() {
    return representation;
  }
}
