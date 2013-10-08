package brennus.eval;

abstract public class Expression {

  public int evalInt(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public long evalLong(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public float evalFloat(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public double evalDouble(Parameters p) {
    throw new UnsupportedOperationException();
  }

  public boolean evalBoolean(Parameters p) {
    throw new UnsupportedOperationException();
  }
}
