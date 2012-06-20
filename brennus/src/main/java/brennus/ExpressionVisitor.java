package brennus;


public interface ExpressionVisitor {

  void visit(GetExpression getFieldExpression);

  void visit(CallMethodExpression callMethodExpression);

  void visit(LiteralExpression literalExpression);

}
