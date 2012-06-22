package brennus;

abstract public class BaseTestClass {

  public RuntimeException error() {
    return new RuntimeException("param not valid");
  }

  abstract public Object get(int i);

}
