package brennus.model;


public interface ExpressionVisitor {

  void visit(GetExpression getFieldExpression);

  void visit(CallMethodExpression callMethodExpression);

  void visit(LiteralExpression literalExpression);

  void visit(BinaryExpression binaryExpression);

  void visit(UnaryExpression unaryExpression);

  void visit(InstanceOfExpression instanceOfExpression);

  void visit(CastExpression castExpression);

}
