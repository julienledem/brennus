package brennus.asm.ref;

import brennus.BaseTestClass;
import brennus.asm.BaseClass;

public class ReferenceClass extends BaseClass {

  private String a;
  private int b;
  private long c;
  private float d;
  private double e;
  private byte f;
  private char g;
  private boolean h;
  private short i;

  @Override
  public Object get(int index) {
    println("get");
    println(index);
    switch (index) {
    case 0:
      return a;
    case 1:
      return b;
    case 2:
      return c;
    case 3:
      return d;
    case 4:
      return e;
    case 5:
      return f;
    case 6:
      return g;
    case 7:
      return h;
    case 8:
      return i;
    default:
      throw error();
    }
  }

  @Override
  public void set(int index, Object o) {
    println("set");
    println(index);
    switch (index) {
    case 0:
      a = (String) o;
      break;
    case 1:
      b = (Integer) o;
      break;
    case 2:
      c = (Long) o;
      break;
    case 3:
      d = (Float) o;
      break;
    case 4:
      e = (Double) o;
      break;
    case 5:
      f = (Byte) o;
      break;
    case 6:
      g = (Character) o;
      break;
    case 7:
      h = (Boolean) o;
      break;
    case 8:
      i = (Short) o;
      break;
    default:
      throw error();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof BaseClass) {
      return equalOrBothNull(((BaseClass)o).get(0),a)
          && equalOrBothNull(((BaseClass)o).get(1),b)
          && equalOrBothNull(((BaseClass)o).get(2),c)
          && equalOrBothNull(((BaseClass)o).get(3),d)
          && equalOrBothNull(((BaseClass)o).get(4),e)
          && equalOrBothNull(((BaseClass)o).get(5),f)
          && equalOrBothNull(((BaseClass)o).get(6),g)
          && equalOrBothNull(((BaseClass)o).get(7),h)
          && equalOrBothNull(((BaseClass)o).get(8),i);
    }
    return false;
  }

}
