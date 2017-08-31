package brennus.asm.specializer.eval;

import brennus.asm.specializer.Specialized;
import brennus.asm.specializer.eval.Context.Variable;

public class SetVar implements Statement {
  private final Variable variable;
  @Specialized
  private final Expr expr;
  
  public SetVar(Variable variable, Expr expr) {
    this.variable = variable;
    this.expr = expr;
  }

  @Override
  public void eval() {
    double eval = expr.eval();
    variable.set(eval);
  }
}
