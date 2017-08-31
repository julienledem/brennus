package brennus.asm.specializer.eval;

import brennus.asm.specializer.eval.Context.Variable;

public class GetVar implements Expr {

  private final Variable variable;
  
  public GetVar(Variable variable) {
    super();
    this.variable = variable;
  }

  @Override
  public double eval() {
    return variable.get();
  }

}
