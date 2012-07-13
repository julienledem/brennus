package brennus;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.*;
import static brennus.model.Protection.*;
import static junit.framework.Assert.*;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

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
//              .call("println").param().literal("sign() => 0").end().endCall().end()
              .returnExp().literal(0).end()
            .endIf()
            .ifExp().get("i").isGreaterThan().literal(0).end()
//              .call("println").param().literal("sign() => 1").end().endCall().end()
              .returnExp().literal(1).end()
            .elseBlock()
//              .call("println").param().literal("sign() => -1").end().endCall().end()
              .returnExp().literal(-1).end()
            .endIf()
          .endMethod()

          .startMethod(PUBLIC, INT, "inc").param(INT, "i")
            .returnExp().get("i").add().literal(1).end()
          .endMethod()

          .startMethod(PUBLIC, BOOLEAN, "not").param(BOOLEAN, "b")
            .call("println").param().get("b").end().endCall().end()
            .call("println").param().get("b").not().end().endCall().end()
            .returnExp().get("b").not().end()
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
                .call("println").param().literal("get(0)").end().endCall().end()
                .returnExp().call("getFoo").endCall().end()
              .endCase()
              .caseBlock(1)
                .call("println").param().literal("get(1)").end().endCall().end()
                .returnExp().call("getBar").endCall().end()
              .endCase()
              .defaultCase()
                .call("println").param().literal("get(?)").end().endCall().end()
                .throwExp().call("error").endCall().end()
              .endCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, VOID, "set").param(INT, "i").param(OBJECT, "o")
            .switchOn().get("i").end()
              .caseBlock(0)
                .call("println").param().literal("set(0)").end().endCall().end()
                .set("foo").get("o").end()
              .breakCase()
              .caseBlock(1)
                .call("println").param().literal("set(1)").end().endCall().end()
                .call("println").param().get("o").end().endCall().end()
                .set("bar").get("o").end()
              .breakCase()
              .defaultCase()
                .call("println").param().literal("set(?)").end().endCall().end()
                .throwExp().call("error").endCall().end()
              .breakCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, BOOLEAN, "equals").param(OBJECT, "o")
            .ifExp().get("o").instanceOf(existing(BaseTestClass.class)).end()
            .returnExp()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseTestClass.class))
                   .call("get").param().literal(0).end().endCall().end()
                .param().get("foo").end()
                .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseTestClass.class))
                   .call("get").param().literal(1).end().endCall().end()
                .param().get("bar").end()
                .endCall()
              .end()
            .elseBlock()
              .returnExp().literal(false).end()
            .endIf()
          .endMethod()
        .endClass();

    new TypePrinter().print(testClass);

    Logger.getLogger("brennus").setLevel(Level.FINEST);
    Logger.getLogger("brennus").addHandler(new Handler() {
      @Override
      public void publish(LogRecord record) {
        System.out.println(record.getMessage());
      }

      @Override
      public void flush() {
        System.out.flush();
      }

      @Override
      public void close() throws SecurityException {
        System.out.flush();
      }
    });
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

    assertFalse(tc.not(true));
    assertTrue(tc.not(false));

    TestClass other = new TestClass();
    other.set(0, "test");
    other.set(1, 123);
    assertTrue(tc.equals(other));
    other.set(1, 124);
    assertFalse(tc.equals(other));
    other.set(1, 123);
    other.set(0, "test2");
    assertFalse(tc.equals(other));
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
