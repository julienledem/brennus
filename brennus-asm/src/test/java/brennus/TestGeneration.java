package brennus;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.*;
import static brennus.model.Protection.*;
import junit.framework.Assert;
import brennus.asm.ASMTypeGenerator;
import brennus.model.FutureType;
import brennus.printer.TypePrinter;

import org.junit.Test;

public class TestGeneration {

  public static class DynamicClassLoader extends ClassLoader {
    ASMTypeGenerator asmTypeGenerator = new ASMTypeGenerator();

    public Class<?> define(FutureType type) {
      byte[] classBytes = asmTypeGenerator.generate(type);
      return super.defineClass(type.getName(), classBytes, 0, classBytes.length);
    }
  }

  @Test
  public void testGeneration() throws Exception {
    FutureType testClass =
        startClass("test.TestClass").extendsType(existing(BaseTestClass.class))

          .field(STRING, "foo", PRIVATE)
          .field(INT, "bar", PRIVATE)

          .startMethod(INT, "inc", PUBLIC).param(INT, "i")
            .returnExp().get("i").add().literal(1).end()
          .endMethod()

          .startMethod(INT, "plus6", PUBLIC).param(INT, "i")
            .returnExp().get("i").add().literal(1).add().literal(2).add().literal(3).end()
          .endMethod()

          .startMethod(STRING, "getFoo", PUBLIC)
            .returnExp().get("foo").end()
          .endMethod()

          .startMethod(INT, "getBar", PUBLIC)
            .returnExp().get("bar").end()
          .endMethod()

          .startMethod(OBJECT, "get", PUBLIC).param(INT, "i")
            .switchOn().get("i").end()
              .caseBlock(0)
                .call("println").withParam().literal("get(0)").end()
                .returnExp().call("getFoo").end()
              .endCase()
              .caseBlock(1)
                .call("println").withParam().literal("get(1)").end()
                .returnExp().call("getBar").end()
              .endCase()
              .defaultCase()
                .call("println").withParam().literal("get(?)").end()
                .throwExp().call("error").end()
              .endCase()
            .endSwitch()
          .endMethod()

          .startMethod(VOID, "set", PUBLIC).param(INT, "i").param(OBJECT, "o")
            .switchOn().get("i").end()
              .caseBlock(0)
                .call("println").withParam().literal("set(0)").end()
                .set("foo").get("o").end()
              .breakCase()
              .caseBlock(1)
                .call("println").withParam().literal("set(1)").end()
                .call("println").withParam().get("o").end()
                .set("bar").get("o").end()
              .breakCase()
              .defaultCase()
                .call("println").withParam().literal("set(?)").end()
                .throwExp().call("error").end()
              .breakCase()
            .endSwitch()
          .endMethod()

        .endClass();

    new TypePrinter().print(testClass);

    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(testClass);

    Class<?> loadClass = cl.loadClass(testClass.getName());
    print(loadClass);
    print("- Constructors");
    printAll(loadClass.getDeclaredConstructors());
    print("- Fields");
    printAll(loadClass.getDeclaredFields());
    print("- Methods");
    printAll(loadClass.getDeclaredMethods());
    print();

    Object test = loadClass.newInstance();
    print(test.getClass());
    BaseTestClass tc = (BaseTestClass)test;
    Assert.assertNull(tc.get(0));
    Assert.assertEquals(0, tc.get(1));

    Assert.assertEquals(2, tc.inc(1));
    Assert.assertEquals(5, tc.inc(4));
    Assert.assertEquals(10, tc.plus6(4));

    tc.set(0, "test");
    tc.set(1, 123);

    Assert.assertEquals("test", tc.get(0));
    Assert.assertEquals(123, tc.get(1));

  }

  private void print() {
    System.out.println();
  }

  private <T> void print(T object) {
    System.out.println(object);
  }

  private <T extends Object> void printAll(T[] objects) {
    for (Object object : objects) {
      print(object);
    }
  }
}
