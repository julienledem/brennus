package brennus.asm.specializer.eval;

public class Const implements Expr {

  private double val;
  
  public Const(double val) {
    super();
    this.val = val;
  }

  @Override
  public double eval() {
    return val;
  }

}
