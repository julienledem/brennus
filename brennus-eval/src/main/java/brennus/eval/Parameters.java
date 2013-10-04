package brennus.eval;

import java.util.Arrays;

public interface Parameters {
  int getInt(int index);
  long getLong(int index);
  double getDouble(int index);
  float getFloat(int index);
  boolean getBoolean(int index);

  public static class ParametersBase implements Parameters {
    @Override
    public int getInt(int index) {
      throw new UnsupportedOperationException("NYI");
    }

    @Override
    public long getLong(int index) {
      throw new UnsupportedOperationException("NYI");
    }

    @Override
    public double getDouble(int index) {
      throw new UnsupportedOperationException("NYI");
    }

    @Override
    public float getFloat(int index) {
      throw new UnsupportedOperationException("NYI");
    }

    @Override
    public boolean getBoolean(int index) {
      throw new UnsupportedOperationException("NYI");
    }

  }

  public static class ParametersArray extends ParametersBase {
    private final Object[] params;

    public ParametersArray(Object... params) {
      super();
      this.params = params;
    }

    @Override
    public int getInt(int index) {
      return (Integer) params[index];
    }

    @Override
    public long getLong(int index) {
      return (Long) params[index];
    }

    @Override
    public double getDouble(int index) {
      return (Double) params[index];
    }

    @Override
    public float getFloat(int index) {
      return (Float) params[index];
    }

    @Override
    public boolean getBoolean(int index) {
      return (Boolean) params[index];
    }

    @Override
    public String toString() {
      return Arrays.toString(params);
    }
  }
}