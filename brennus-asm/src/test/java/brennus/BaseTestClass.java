package brennus;

abstract public class BaseTestClass {

  public RuntimeException error() {
    return new RuntimeException("param not valid");
  }

  public void println(Object o) {
    System.out.println(o);
  }

  abstract public int inc(int i);

  abstract public int plus6(int i);

  abstract public Object get(int i);

  abstract public void set(int i, Object o);

  abstract public int sign(int i);

  abstract public boolean not(boolean i);

  public boolean equalOrBothNull(Object o1, Object o2) {
    return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
  }
}
