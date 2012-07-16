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
        startClass("brennus.asm.TestTuple$TestClass").extendsType(existing(BaseClass.class))
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
            .call("println").param().literal("get").end().endCall().end()
            .call("println").param().get("index").end().endCall().end()
            .switchOn().get("index").end()
              .caseBlock(0)
                .returnExp().get("a").end()
              .endCase()
              .caseBlock(1)
                .returnExp().get("b").end()
              .endCase()
              .caseBlock(2)
                .returnExp().get("c").end()
              .endCase()
              .caseBlock(3)
                .returnExp().get("d").end()
              .endCase()
              .caseBlock(4)
                .returnExp().get("e").end()
              .endCase()
              .caseBlock(5)
                .returnExp().get("f").end()
              .endCase()
              .caseBlock(6)
                .returnExp().get("g").end()
              .endCase()
              .caseBlock(7)
                .returnExp().get("h").end()
              .endCase()
              .caseBlock(8)
                .returnExp().get("i").end()
              .endCase()
              .defaultCase()
                .throwExp().call("error").endCall().end()
              .endCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, VOID, "set").param(INT, "index").param(OBJECT, "o")
            .call("println").param().literal("set").end().endCall().end()
            .call("println").param().get("index").end().endCall().end()
            .switchOn().get("index").end()
              .caseBlock(0)
                .set("a").get("o").end()
              .breakCase()
              .caseBlock(1)
                .set("b").get("o").end()
              .breakCase()
              .caseBlock(2)
                .set("c").get("o").end()
              .breakCase()
              .caseBlock(3)
                .set("d").get("o").end()
              .breakCase()
              .caseBlock(4)
                .set("e").get("o").end()
              .breakCase()
              .caseBlock(5)
                .set("f").get("o").end()
              .breakCase()
              .caseBlock(6)
                .set("g").get("o").end()
              .breakCase()
              .caseBlock(7)
                .set("h").get("o").end()
              .breakCase()
              .caseBlock(8)
                .set("i").get("o").end()
              .breakCase()
              .defaultCase()
                .throwExp().call("error").endCall().end() // this line number is checked later
              .breakCase()
            .endSwitch()
          .endMethod()

          .startMethod(PUBLIC, BOOLEAN, "equals").param(OBJECT, "o")
            .ifExp().get("o").instanceOf(existing(BaseClass.class)).end()
            .returnExp()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(0).end().endCall().end()
                .param().get("a").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(1).end().endCall().end()
                .param().get("b").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(2).end().endCall().end()
                .param().get("c").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(3).end().endCall().end()
                .param().get("d").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(4).end().endCall().end()
                .param().get("e").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(5).end().endCall().end()
                .param().get("f").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(6).end().endCall().end()
                .param().get("g").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(7).end().endCall().end()
                .param().get("h").end()
              .endCall()
              .and()
              .call("equalOrBothNull")
                .param().get("o").castTo(existing(BaseClass.class))
                   .call("get").param().literal(8).end().endCall().end()
                .param().get("i").end()
              .endCall()
              .end()
            .elseBlock()
              .returnExp().literal(false).end()
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
    for (BaseClass t : new BaseClass[]{o1,o2}) {
      t.set(0, "test");
      t.set(1, 12);
      t.set(2, 22l);
      t.set(3, 32f);
      t.set(4, 42d);
      t.set(5, (byte)52);
      t.set(6, 'a');
      t.set(7, true);
      t.set(8, (short)82);
    }
    Assert.assertEquals(o2, o1);
    Assert.assertEquals(o1, o2);
    o1.set(7, false);
    Assert.assertFalse(o2.equals(o1));
    Assert.assertFalse(o1.equals(o2));
  }
}
