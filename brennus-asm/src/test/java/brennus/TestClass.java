package brennus;

public class TestClass extends BaseTestClass {
  private String foo;
  private int bar;

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
}
