package brennus.asm;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.*;
import static brennus.model.Protection.*;
import static junit.framework.Assert.*;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import brennus.BaseTestClass;
import brennus.TestClass;
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
            .ifExp().get("i").isEqualTo().literal(0).thenBlock()
//              .exec().call("println").param().literal("sign() => 0").endParam().endCall().endExec()
              .returnExp().literal(0).endReturn()
            .endIf()
            .ifExp().get("i").isGreaterThan().literal(0).thenBlock()
//              .exec().call("println").param().literal("sign() => 1").endParam().endCall().endExec()
              .returnExp().literal(1).endReturn()
            .elseBlock()
//              .exec().call("println").param().literal("sign() => -1").endParam().endCall().endExec()
              .returnExp().literal(-1).endReturn()
            .endIf()
          .endMethod()

          .startMethod(PUBLIC, INT, "inc").param(INT, "i")
            .returnExp().get("i").add().literal(1).endReturn()
          .endMethod()

          .startMethod(PUBLIC, BOOLEAN, "not").param(BOOLEAN, "b")
            .exec().call("println").param().get("b").endParam().endCall().endExec()
            .exec().call("println").param().get("b").not().endParam().endCall().endExec()
            .returnExp().get("b").not().endReturn()
          .endMethod()

          .startMethod(PUBLIC, INT, "plus6").param(INT, "i")
            .returnExp().get("i").add().literal(1).add().literal(2).add().literal(3).endReturn()
          .endMethod()

          .startMethod(PUBLIC, STRING, "getFoo")
            .returnExp().get("foo").endReturn()
          .endMethod()

          .startMethod(PUBLIC, INT, "getBar")
            .returnExp().get("bar").endReturn()
          .endMethod()

          .startMethod(PUBLIC, OBJECT, "get").param(INT, "i")
            .switchOn().get("i").endSwitchOn()
              .caseBlock(0)
                .exec().call("println").param().literal("get(0)").endParam().endCall().endExec()
                .returnExp().call("getFoo").endCall().endReturn()
              .endCase()
              .caseBlock(1)
                .exec().call("println").param().literal("get(1)").endParam().endCall().endExec()
                .returnExp().call("getBar").endCall().endReturn()
              .endCase()
              .defaultCase()
                .exec().call("println").param().literal("get(?)").endParam().endCall().endExec()
                .throwExp().call("error").endCall().endThrow()
              .endCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, VOID, "set").param(INT, "i").param(OBJECT, "o")
            .switchOn().get("i").endSwitchOn()
              .caseBlock(0)
                .exec().call("println").param().literal("set(0)").endParam().endCall().endExec()
                .set("foo").get("o").endSet()
              .breakCase()
              .caseBlock(1)
                .exec().call("println").param().literal("set(1)").endParam().endCall().endExec()
                .exec().call("println").param().get("o").endParam().endCall().endExec()
                .set("bar").get("o").endSet()
              .breakCase()
              .defaultCase()
                .exec().call("println").param().literal("set(?)").endParam().endCall().endExec()
                .throwExp().call("error").endCall().endThrow()
              .breakCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, BOOLEAN, "equals").param(OBJECT, "o")
            .ifExp().get("o").instanceOf(existing(BaseTestClass.class)).thenBlock()
            .returnExp()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseTestClass.class))
                   .call("get").param().literal(0).endParam().endCall()
                .endParam()
                .param().get("foo").endParam()
                .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseTestClass.class))
                   .call("get").param().literal(1).endParam().endCall()
                .endParam()
                .param().get("bar").endParam()
                .endCall()
              .endReturn()
            .elseBlock()
              .returnExp().literal(false).endReturn()
            .endIf()
          .endMethod()
        .endClass();

    new TypePrinter().print(testClass);

    Logger.getLogger("brennus").setLevel(Level.FINEST);
    Logger.getLogger("brennus").addHandler(new Handler() {
      public void publish(LogRecord record) {
        System.out.println(record.getMessage());
      }
      public void flush() {
        System.out.flush();
      }
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
