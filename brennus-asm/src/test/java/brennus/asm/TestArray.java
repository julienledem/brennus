package brennus.asm;

import static brennus.model.ExistingType.INT;
import static brennus.model.ExistingType.VOID;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import static org.junit.Assert.assertEquals;
import brennus.Builder;
import brennus.model.FutureType;
import brennus.printer.TypePrinter;

import org.junit.Test;

public class TestArray {

  DynamicClassLoader cl = new DynamicClassLoader();

  abstract public static class ArrayWrapperBase {
    abstract public void set(int[] values);
    abstract public int size();
    abstract public int get(int index);
    abstract public int[] newArray(int size);
  }

  public static class ArrayWrapper extends ArrayWrapperBase {

    int[] values;

    @Override
    public void set(int[] values) {
      this.values = values;
    }

    @Override
    public int size() {
      return values.length;
    }

    @Override
    public int get(int index) {
      return values[index];
    }

    @Override
    public int[] newArray(int size) {
      return new int[size];
    }
  }

  @Test
  public void test() throws InstantiationException, IllegalAccessException {
    FutureType t = new Builder().startClass("CompiledArrayWrapper", existing(ArrayWrapperBase.class))
    .field(PUBLIC, existing(int[].class), "values")
    .startMethod(PUBLIC, VOID, "set").param(existing(int[].class), "v")
      .set("values").get("v").endSet()
    .endMethod()
    .startMethod(PUBLIC, INT, "size")
      .returnExp().get("values").arraySize().endReturn()
    .endMethod()
    .startMethod(PUBLIC, INT, "get").param(INT, "index")
      .returnExp().get("values").getArrayValueAt().get("index").endGetArrayValue().endReturn()
    .endMethod()
    .startMethod(PUBLIC, existing(int[].class), "newArray").param(INT, "size")
      .returnExp().newArrayOfSize(existing(int.class)).get("size").endNewArray().endReturn()
    .endMethod()
    .endClass();

    new TypePrinter().print(t);

    ArrayWrapperBase i = (ArrayWrapperBase)cl.define(t).newInstance();

    ArrayWrapperBase[] as = { new ArrayWrapper(), i };
    for (ArrayWrapperBase a : as) {
      a.set(new int[] { 1, 2 , 3});
      assertEquals(3, a.size());
      assertEquals(3, a.get(2));
      a.set(a.newArray(1));
      assertEquals(1, a.size());
    }
  }

}
