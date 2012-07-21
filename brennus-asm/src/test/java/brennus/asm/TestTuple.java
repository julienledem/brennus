package brennus.asm;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.BOOLEAN;
import static brennus.model.ExistingType.BYTE;
import static brennus.model.ExistingType.CHAR;
import static brennus.model.ExistingType.DOUBLE;
import static brennus.model.ExistingType.FLOAT;
import static brennus.model.ExistingType.INT;
import static brennus.model.ExistingType.LONG;
import static brennus.model.ExistingType.OBJECT;
import static brennus.model.ExistingType.SHORT;
import static brennus.model.ExistingType.STRING;
import static brennus.model.ExistingType.VOID;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PRIVATE;
import static brennus.model.Protection.PUBLIC;
import brennus.asm.TestGeneration.DynamicClassLoader;
import brennus.asm.ref.ReferenceClass;
import brennus.model.FutureType;

import org.junit.Assert;
import org.junit.Test;

public class TestTuple {

  @Test
  public void testTuple() throws Exception {
    Class<? extends BaseClass> c = ReferenceClass.class;
    testSetGet(c);

    FutureType testClass =
        startClass("brennus.asm.TestTuple$TestClass", existing(BaseClass.class))
          .field(PRIVATE, STRING, "a")
          .field(PRIVATE, INT, "b")
          .field(PRIVATE, LONG, "c")
          .field(PRIVATE, FLOAT, "d")
          .field(PRIVATE, DOUBLE, "e")
          .field(PRIVATE, BYTE, "f")
          .field(PRIVATE, CHAR, "g")
          .field(PRIVATE, BOOLEAN, "h")
          .field(PRIVATE, SHORT, "i")


          .startMethod(PUBLIC, OBJECT, "get").param(INT, "index")
            .exec().callOnThis("println").literal("get").endCall().endExec()
            .exec().callOnThis("println").get("index").endCall().endExec()
            .switchOn().get("index").switchBlock()
              .caseBlock(0)
                .returnExp().get("a").endReturn()
              .endCase()
              .caseBlock(1)
                .returnExp().get("b").endReturn()
              .endCase()
              .caseBlock(2)
                .returnExp().get("c").endReturn()
              .endCase()
              .caseBlock(3)
                .returnExp().get("d").endReturn()
              .endCase()
              .caseBlock(4)
                .returnExp().get("e").endReturn()
              .endCase()
              .caseBlock(5)
                .returnExp().get("f").endReturn()
              .endCase()
              .caseBlock(6)
                .returnExp().get("g").endReturn()
              .endCase()
              .caseBlock(7)
                .returnExp().get("h").endReturn()
              .endCase()
              .caseBlock(8)
                .returnExp().get("i").endReturn()
              .endCase()
              .defaultCase()
                .throwExp().callOnThisNoParam("error").endThrow()
              .endCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, VOID, "set").param(INT, "index").param(OBJECT, "o")
            .exec().callOnThis("println").literal("set").endCall().endExec()
            .exec().callOnThis("println").get("index").endCall().endExec()
            .switchOn().get("index").switchBlock()
              .caseBlock(0)
                .set("a").get("o").endSet()
              .breakCase()
              .caseBlock(1)
                .set("b").get("o").endSet()
              .breakCase()
              .caseBlock(2)
                .set("c").get("o").endSet()
              .breakCase()
              .caseBlock(3)
                .set("d").get("o").endSet()
              .breakCase()
              .caseBlock(4)
                .set("e").get("o").endSet()
              .breakCase()
              .caseBlock(5)
                .set("f").get("o").endSet()
              .breakCase()
              .caseBlock(6)
                .set("g").get("o").endSet()
              .breakCase()
              .caseBlock(7)
                .set("h").get("o").endSet()
              .breakCase()
              .caseBlock(8)
                .set("i").get("o").endSet()
              .breakCase()
              .defaultCase()
                .throwExp().callOnThisNoParam("error").endThrow() // this line number is checked later
              .breakCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, BOOLEAN, "equals").param(OBJECT, "o")
          // if (o instanceOf BaseClass) {
            .ifExp().get("o").instanceOf(existing(BaseClass.class)).thenBlock()
            .returnExp()
            // equalOrBothNull(
              .callOnThis("equalOrBothNull")
                 // ((BaseClass)o)
                .get("o").castTo(existing(BaseClass.class))
                // .get(0)
                   .call("get").literal(0).endCall()
                   //  , a
                .nextParam().get("a")
               // )
              .endCall()
              // &&
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(1).endCall()
                .nextParam().get("b")
              .endCall()
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(2).endCall()
                .nextParam().get("c")
              .endCall()
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(3).endCall()
                .nextParam().get("d")
              .endCall()
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(4).endCall()
                .nextParam().get("e")
              .endCall()
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(5).endCall()
                .nextParam().get("f")
              .endCall()
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(6).endCall()
                .nextParam().get("g")
              .endCall()
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(7).endCall()
                .nextParam().get("h")
              .endCall()
              .and()
              .callOnThis("equalOrBothNull")
                .get("o").castTo(existing(BaseClass.class))
                   .call("get").literal(8).endCall()
                .nextParam().get("i")
              .endCall()
              .endReturn()
            .elseBlock()
              .returnExp().literal(false).endReturn()
            .endIf()
          .endMethod()

        .endClass();

//    new TypePrinter().print(testClass);

    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(testClass);
    Class<?> generated = (Class<?>)cl.loadClass("brennus.asm.TestTuple$TestClass");
    testSetGet(generated);

    testEquals((BaseClass)generated.newInstance(), (BaseClass)c.newInstance());
  }

  private void testSetGet(Class<?> c) throws Exception {
    BaseClass t = (BaseClass)c.newInstance();
    t.set(0, "test");
    Assert.assertEquals("test", t.get(0));
    t.set(1, 12);
    Assert.assertEquals(12, t.get(1));
    t.set(2, 22l);
    Assert.assertEquals(22l, t.get(2));
    t.set(3, 32f);
    Assert.assertEquals(32f, t.get(3));
    t.set(4, 42d);
    Assert.assertEquals(42d, t.get(4));
    t.set(5, (byte)52);
    Assert.assertEquals((byte)52, t.get(5));
    t.set(6, 'a');
    Assert.assertEquals('a', t.get(6));
    t.set(7, true);
    Assert.assertEquals(true, t.get(7));
    t.set(7, false);
    Assert.assertEquals(false, t.get(7));
    t.set(8, (short)82);
    Assert.assertEquals((short)82, t.get(8));
    if (!(t instanceof ReferenceClass)) {
      try {
        t.set(123, null);
      } catch (RuntimeException e) {
        Assert.assertEquals("TestTuple.java", e.getStackTrace()[1].getFileName());
        Assert.assertEquals(114, e.getStackTrace()[1].getLineNumber());
        // checking that we display the line number from the builder class
        e.printStackTrace();
      }
    }
  }

  private void testEquals(BaseClass o1, BaseClass o2) {
    Object[] good = { "test", 12, 22l, 32f, 42d, (byte)52, 'a', true,  (short)82 };
    Object[] bad  = { "foo",  13, 23l, 33f, 43d, (byte)53, 'b', false, (short)83 };
    for (BaseClass t : new BaseClass[]{o1,o2}) {
      for (int i = 0; i <= 8; i++) {
        t.set(0, good[0]);
      }
    }
    Assert.assertEquals(o2, o1);
    Assert.assertEquals(o1, o2);
    for (int i = 0; i <= 8; i++) {
      o1.set(i, bad[i]);
      Assert.assertFalse(o2.equals(o1));
      Assert.assertFalse(o1.equals(o2));
      o1.set(i, good[i]);
    }
  }
}
