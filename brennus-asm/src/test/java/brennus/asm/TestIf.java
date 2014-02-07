package brennus.asm;

import static brennus.model.ExistingType.*;
import static brennus.model.ExistingType.INT;
import static brennus.model.Protection.PUBLIC;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import brennus.Builder;
import brennus.model.ExistingType;
import brennus.model.FutureType;

import org.junit.Test;

public class TestIf {
  DynamicClassLoader cl = new DynamicClassLoader();

  public static abstract class Base {

    public void print(Object o) {
      System.out.println(o);
    }

    abstract public boolean exec1(boolean param);

    abstract public boolean exec2(int i, int j);

    abstract public boolean exec3(Object o);

  }

  private Base end(FutureType t) throws Exception {
    return (Base) cl.define(t).newInstance();
  }

  @Test
  public void testIfNot() throws Exception {
    Base b = end(
        new Builder().startClass("Foo", ExistingType.existing(Base.class))
          .startMethod(PUBLIC, BOOLEAN, "exec1").param(BOOLEAN, "param")
            .ifExp().not().get("param").thenBlock()
              .returnExp().literal(true).endReturn()
            .elseBlock()
              .returnExp().literal(false).endReturn()
            .endIf()
          .endMethod()
        .endClass());

    assertTrue(b.exec1(false));
    assertFalse(b.exec1(true));
  }

  @Test
  public void testIfGT() throws Exception {
    Base b = end(
        new Builder().startClass("Foo", ExistingType.existing(Base.class))
          .startMethod(PUBLIC, BOOLEAN, "exec2").param(INT, "i").param(INT, "j")
            .ifExp().get("i").isGreaterThan().get("j").thenBlock()
              .returnExp().literal(true).endReturn()
            .elseBlock()
              .returnExp().literal(false).endReturn()
            .endIf()
          .endMethod()
        .endClass());

    assertTrue(b.exec2(2, 1));
    assertFalse(b.exec2(1, 2));
  }

  @Test
  public void testIfEQ() throws Exception {
    Base b = end(
        new Builder().startClass("Foo", ExistingType.existing(Base.class))
          .startMethod(PUBLIC, BOOLEAN, "exec2").param(INT, "i").param(INT, "j")
            .ifExp().get("i").isEqualTo().get("j").thenBlock()
              .returnExp().literal(true).endReturn()
            .elseBlock()
              .returnExp().literal(false).endReturn()
            .endIf()
          .endMethod()
        .endClass());

    assertTrue(b.exec2(1, 1));
    assertFalse(b.exec2(1, 2));
  }

  @Test
  public void testIfNull() throws Exception {
    Base b = end(
        new Builder().startClass("Foo", ExistingType.existing(Base.class))
          .startMethod(PUBLIC, BOOLEAN, "exec3").param(OBJECT, "o")
            .ifExp().get("o").isNull().thenBlock()
              .returnExp().literal(true).endReturn()
            .elseBlock()
              .returnExp().literal(false).endReturn()
            .endIf()
          .endMethod()
        .endClass());

    assertTrue(b.exec3(null));
    assertFalse(b.exec3(new Object()));
  }

  @Test
  public void testIfNotNull() throws Exception {
    Base b = end(
        new Builder().startClass("Foo", ExistingType.existing(Base.class))
          .startMethod(PUBLIC, BOOLEAN, "exec3").param(OBJECT, "o")
            .ifExp().get("o").isNotNull().thenBlock()
              .returnExp().literal(true).endReturn()
            .elseBlock()
              .returnExp().literal(false).endReturn()
            .endIf()
          .endMethod()
        .endClass());

    assertTrue(b.exec3(new Object()));
    assertFalse(b.exec3(null));
  }

}
