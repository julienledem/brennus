package brennus.asm.specializer;

public class SpecializationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public SpecializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SpecializationException(String message) {
    super(message);
  }
}
