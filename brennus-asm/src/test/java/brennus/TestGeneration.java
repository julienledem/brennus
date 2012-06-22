package brennus;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.*;
import static brennus.model.Protection.*;
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
          .startMethod(STRING, "getFoo", PUBLIC)
            .expression().get("foo").returnExp()
          .endMethod()
          .startMethod(INT, "getBar", PUBLIC)
            .expression().get("bar").returnExp()
          .endMethod()
          .startMethod(OBJECT, "get", PUBLIC)
            .withParameter(INT, "i")
            .expression().get("i").switchOn()
              .caseBlock(0)
                .expression().call("getFoo").returnExp()
              .endCase()
              .caseBlock(1)
                .expression().call("getBar").returnExp()
              .endCase()
              .defaultCase()
                .expression().call("error").throwException()
              .endCase()
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
    tc.get(1);

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
