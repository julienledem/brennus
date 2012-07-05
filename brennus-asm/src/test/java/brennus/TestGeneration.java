package brennus;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.*;
import static brennus.model.Protection.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
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

          .field(PRIVATE, STRING, "foo")
          .field(PRIVATE, INT, "bar")

          .startMethod(PUBLIC, INT, "sign").param(INT, "i")
            .ifExp().get("i").isEqualTo().literal(0).end()
              .call("println").param().literal("sign() => 0").end()
              .returnExp().literal(0).end()
            .endIf()
            .ifExp().get("i").isGreaterThan().literal(0).end()
              .call("println").param().literal("sign() => 1").end()
              .returnExp().literal(1).end()
            .elseBlock()
              .call("println").param().literal("sign() => -1").end()
              .returnExp().literal(-1).end()
            .endIf()
          .endMethod()

          .startMethod(PUBLIC, INT, "inc").param(INT, "i")
            .returnExp().get("i").add().literal(1).end()
          .endMethod()

          .startMethod(PUBLIC, INT, "plus6").param(INT, "i")
            .returnExp().get("i").add().literal(1).add().literal(2).add().literal(3).end()
          .endMethod()

          .startMethod(PUBLIC, STRING, "getFoo")
            .returnExp().get("foo").end()
          .endMethod()

          .startMethod(PUBLIC, INT, "getBar")
            .returnExp().get("bar").end()
          .endMethod()

          .startMethod(PUBLIC, OBJECT, "get").param(INT, "i")
            .switchOn().get("i").end()
              .caseBlock(0)
                .call("println").param().literal("get(0)").end()
                .returnExp().call("getFoo").end()
              .endCase()
              .caseBlock(1)
                .call("println").param().literal("get(1)").end()
                .returnExp().call("getBar").end()
              .endCase()
              .defaultCase()
                .call("println").param().literal("get(?)").end()
                .throwExp().call("error").end()
              .endCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, VOID, "set").param(INT, "i").param(OBJECT, "o")
            .switchOn().get("i").end()
              .caseBlock(0)
                .call("println").param().literal("set(0)").end()
                .set("foo").get("o").end()
              .breakCase()
              .caseBlock(1)
                .call("println").param().literal("set(1)").end()
                .call("println").param().get("o").end()
                .set("bar").get("o").end()
              .breakCase()
              .defaultCase()
                .call("println").param().literal("set(?)").end()
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
    assertNull(tc.get(0));
    assertEquals(0, tc.get(1));

    assertEquals(2, tc.inc(1));
    assertEquals(5, tc.inc(4));
    assertEquals(10, tc.plus6(4));

    tc.set(0, "test");
    tc.set(1, 123);

    assertEquals("test", tc.get(0));
    assertEquals(123, tc.get(1));

    assertEquals(1, tc.sign(5));
    assertEquals(0, tc.sign(0));
    assertEquals(-1, tc.sign(-15));
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
