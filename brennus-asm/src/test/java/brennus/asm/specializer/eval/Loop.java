package brennus.asm.specializer.eval;

import brennus.asm.specializer.Specialized;

public class Loop implements Statement {

  private final int count;
  @Specialized
  private final Statement body;
  
  public Loop(int count, Statement body) {
    super();
    this.count = count;
    this.body = body;
  }


  @Override
  public void eval() {
    for (int i = 0; i < count; i++) {
      body.eval();
    }
  }

}
