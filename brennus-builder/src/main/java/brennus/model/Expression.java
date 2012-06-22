package brennus.model;

abstract public class Expression {

  abstract public void accept(ExpressionVisitor expressionVisitor);

}
