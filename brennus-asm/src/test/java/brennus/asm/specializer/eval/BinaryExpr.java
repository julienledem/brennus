package brennus.asm.specializer.eval;

import brennus.asm.specializer.Specialized;

public abstract class BinaryExpr implements Expr {

  @Specialized
  protected Expr left;
  @Specialized
  protected Expr right;
  
  public BinaryExpr(Expr left, Expr right) {
    super();
    this.left = left;
    this.right = right;
  }
  
}
