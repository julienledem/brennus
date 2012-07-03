package brennus.asm;

abstract public class BaseClass {

  public RuntimeException error() {
    return new RuntimeException("error");
  }

  public void println(Object o) {
    System.out.println(o);
  }

  abstract public Object get(int i);

  abstract public void set(int i, Object o);

}
