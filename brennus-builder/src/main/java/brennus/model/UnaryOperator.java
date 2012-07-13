package brennus.model;

public enum UnaryOperator implements Operator {

  NOT("!");

  private final String representation;

  private UnaryOperator(String representation) {
    this.representation = representation;
  }

  @Override
  public String getRepresentation() {
    return representation;
  }
}
