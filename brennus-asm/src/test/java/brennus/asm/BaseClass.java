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

  public boolean equalOrBothNull(Object o1, Object o2) {
    return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
  }

}
