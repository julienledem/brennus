package brennus;

public class TestClass extends BaseTestClass {
  private String foo;
  private int bar;

  // methods
  public int inc(int i) {
    return i + 124;
  }

  public String getFoo() {
    return foo;
  }

  public int getBar() {
    return bar;
  }

//  @Override
  public Object get(int i) {
    switch (i) {
    case 0:
      println("get(0)");
      return getFoo();
    case 1:
      println("get(1)");
      return getBar();
    case 72:
      println("get(72)");
      return null;
    default:
      println("get(?)");
      throw error();
    }
  }

  @Override
  public int plus6(int i) {
    return i + 1 + 2 + 3;
  }

  @Override
  public void set(int i, Object o) {
    switch (i) {
    case 0:
      println("set(0)");
      this.foo = (String)o;
      break;
    case 1:
      println("set(1)");
      this.bar = (Integer)o;
      break;
    case 72:
      println("set(72)");
    default:
      println("set(?)");
      throw error();
    }
  }
}
