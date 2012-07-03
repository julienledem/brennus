package brennus.asm.ref;

import brennus.asm.BaseClass;

public class ReferenceClass extends BaseClass {

  private String a;
  private int b;

  @Override
  public Object get(int i) {
    println("get");
    println(i);
    switch (i) {
    case 0:
      return a;
    case 1:
      return b;
    default:
      throw error();
    }
  }

  @Override
  public void set(int i, Object o) {
    println("set");
    println(i);
    switch (i) {
    case 0:
      a = (String) o;
      break;
    case 1:
      b = (Integer) o;
      break;
    default:
      throw error();
    }
  }

}
