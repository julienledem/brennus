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
      return getFoo();
    case 1:
      return getBar();
    case 72:
      return null;
    default:
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
      this.foo = (String)o;
    case 1:
      this.bar = (Integer)o;
    case 72:
    default:
      throw error();
    }
  }
}
