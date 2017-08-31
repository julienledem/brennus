package brennus.asm.specializer.eval;

public class Div extends BinaryExpr {

  public Div(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public double eval() {
    return left.eval() / right.eval();
  }

}
