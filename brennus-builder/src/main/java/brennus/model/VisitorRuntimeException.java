package brennus.model;

public class VisitorRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public VisitorRuntimeException(String visitor, Object visited, Throwable targetException) {
    super(visitor + ".visit("+visited+")", targetException);
  }

}
