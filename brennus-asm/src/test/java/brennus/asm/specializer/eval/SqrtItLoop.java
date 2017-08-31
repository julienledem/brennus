package brennus.asm.specializer.eval;

import brennus.asm.specializer.eval.Context.Variable;

public class SqrtItLoop implements Statement {
  private final Variable variable;
  private final int count;
  
  public SqrtItLoop(int count, Variable variable) {
    this.count = count;
    this.variable = variable;
  }

  @Override
  public void eval() {
    double a = variable.get();
    for (int i = 0; i < count; i++) {
      a = (a + 2/a)/2;
    }
    variable.set(a);
  }
}
