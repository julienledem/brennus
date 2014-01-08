package brennus.model;


public interface ExpressionVisitor {

  void visit(GetExpression e);

  void visit(CallMethodExpression e);

  void visit(LiteralExpression e);

  void visit(BinaryExpression e);

  void visit(UnaryExpression e);

  void visit(InstanceOfExpression e);

  void visit(CastExpression e);

  void visit(CallConstructorExpression e);

  void visit(InstantiationExpression e);

  void visit(NewArrayExpression e);

}
