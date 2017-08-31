package brennus.asm.specializer.eval;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Context {
  private Map<String, Variable> names = new HashMap<>();
  
  public Variable getVariable(String name) {
    Variable variable = names.get(name);
    if (variable == null) {
      variable = new Variable();
      names.put(name, variable);
    }
    return variable;
  }
  
  
  public class Variable {
    private double val;

    private Variable() {
      super();
    }
    
    public void set(double val) {
      this.val = val;
    }
    
    public double get() {
      return val;
    }
  }
}
