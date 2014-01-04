package brennus.asm;

import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;
import brennus.Builder;
import brennus.model.FutureType;

import org.junit.Test;

public class TestNewInstance {

  public static class Foo {
    String blah;
    public Foo() {
      blah = "blah";
    }
  }

  @Test
  public void testNew() throws Exception {
    FutureType t = new Builder().startClass(TestNewInstance.class.getName() + "$Bar") // public static class Bar {
      .field(PUBLIC, existing(Foo.class), "baz")                                      //   public Foo baz;
      .startConstructor(PUBLIC)                                                       //   public Bar() {
        .callSuperConstructorNoParam()                                                //     super();
        .set("baz").newInstanceNoParam(existing(Foo.class)).endSet()                  //     baz = new Foo();
      .endConstructor()                                                               //   }
    .endClass();                                                                      // }
    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(t);
    Object instance = cl.loadClass(t.getName()).newInstance();
    Object baz = instance.getClass().getField("baz").get(instance);
    assertNotNull("the instance got created", baz);
    assertTrue("it is of the correct type and not " + baz.getClass(), baz instanceof Foo);
    assertEquals("The constructor actually got called", "blah", ((Foo)baz).blah);
  }

}
