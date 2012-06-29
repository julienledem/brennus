package brennus;

abstract public class BaseTestClass {

  public RuntimeException error() {
    return new RuntimeException("param not valid");
  }

  public void println(String string) {
    System.out.println(string);
  }

  abstract public int inc(int i);

  abstract public int plus6(int i);

  abstract public Object get(int i);

  abstract public void set(int i, Object o);

}
