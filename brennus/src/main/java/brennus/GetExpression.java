package brennus;

public class GetExpression extends Expression {

  private final String fieldName;

  GetExpression(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public void visit(ExpressionVisitor expressionVisitor) {
    expressionVisitor.visit(this);
  }

  public String getFieldName() {
    return fieldName;
  }
}