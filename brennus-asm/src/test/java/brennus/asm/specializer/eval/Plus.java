package brennus.asm.specializer.eval;

public class Plus extends BinaryExpr {

  public Plus(Expr left, Expr right) {
    super(left, right);
  }

  @Override
  public double eval() {
    return left.eval() + right.eval();
  }

}
