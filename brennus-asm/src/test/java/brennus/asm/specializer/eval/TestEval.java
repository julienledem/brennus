package brennus.asm.specializer.eval;

import java.io.IOException;

import org.junit.Test;

import brennus.asm.specializer.Specializer;
import brennus.asm.specializer.eval.Context.Variable;

public class TestEval {
  private static final int COUNT = 10;
  private static final int IT_COUNT = 1000000;
  Context context = new Context();
  
  @Test
  public void all() throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, NoSuchFieldException, IOException {
    test1();
    test2();
    test3();
    test3();
    test2();
    test1();
  }
  
  @Test
  public void test1() {
    Variable a = var();
    aggregate(a, fold(a));
  }
  
  @Test
  public void test3() {
    Variable a = var();
    aggregate(a, new SqrtItLoop(IT_COUNT, a));
  }

  @Test
  public void test2() throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, NoSuchFieldException, IOException {
    Specializer specializer = new Specializer();
    Variable a = var();
    aggregate(a, specializer.specialize(Statement.class, fold(a)));
  }
  
  private Variable var() {
    Variable a = context.getVariable("a");
    a.set(0);
    return a;
  }

  private Statement fold(Variable a) {
    return new Loop(IT_COUNT, new SetVar(
        a, 
        new Div(
            new Plus(
                new GetVar(a), 
                new Div(new Const(2), new GetVar(a))), 
            new Const(2))
        ));
  }

  private void aggregate(Variable a, Statement s) {
    System.out.println(s.getClass().getSimpleName());
    for (int j = 0; j < COUNT; j++) {
      a.set(1.0d);
      long t0 = System.nanoTime();
      s.eval();
      long t1 = System.nanoTime();
      System.out.println(a.get() + " " + (t1 - t0)/1000000.0);
    }
  }
}
