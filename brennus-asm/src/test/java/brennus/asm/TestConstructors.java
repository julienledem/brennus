package brennus.asm;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import brennus.asm.TestGeneration.DynamicClassLoader;
import brennus.model.FutureType;
import brennus.printer.TypePrinter;

public class TestConstructors {

  abstract public static class Parent {

    private String init;

    public Parent() {
      this("default");
    }

    public Parent(String init) {
      this.init = init;
    }

    public String getInit() {
      return init;
    }
  }

  @Test
  public void testConstructorOneParam() throws Exception {
    FutureType testClass1 =
        startClass("brennus.asm.TestConstructors$TestClass1", existing(Parent.class))
          .startConstructor(PUBLIC).param(existing(String.class), "init")
            .callSuperConstructor().get("init").endConstructorCall()
          .endConstructor()
          .endClass();

    new TypePrinter().print(testClass1);
    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(testClass1);
    Class<?> generated = (Class<?>)cl.loadClass("brennus.asm.TestConstructors$TestClass1");

    Parent parent = (Parent)generated.getConstructor(String.class).newInstance("foo");
    assertEquals("foo", parent.getInit());
  }

  @Test
  public void testDefaultConstructor() throws Exception {
    FutureType testClass1 =
        startClass("brennus.asm.TestConstructors$TestClass2", existing(Parent.class))
          .endClass();

    new TypePrinter().print(testClass1);
    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(testClass1);
    Class<?> generated = (Class<?>)cl.loadClass("brennus.asm.TestConstructors$TestClass2");

    Parent parent = (Parent)generated.newInstance();
    assertEquals("default", parent.getInit());
  }
}
