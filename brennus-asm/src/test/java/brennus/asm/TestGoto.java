package brennus.asm;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.VOID;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import brennus.asm.TestGeneration.DynamicClassLoader;
import brennus.model.FutureType;
import brennus.printer.TypePrinter;

import org.junit.Test;

public class TestGoto {

  abstract public static class FSA {

    private List<String> states = new ArrayList<String>();

    abstract public void exec();

    public void state(String p) {
      states.add(p);
    }

    public List<String> getStates() {
      return states;
    }
  }

  @Test
  public void testGoto() throws Exception {

    FutureType testClass =
        startClass("brennus.asm.TestGoto$TestClass", existing(FSA.class))
          .startMethod(PUBLIC, VOID, "exec")
            .label("a")
            .exec().callOnThis("state").literal("a").endCall().endExec()
            .gotoLabel("c")
            .label("b")
            .exec().callOnThis("state").literal("b").endCall().endExec()
            .gotoLabel("end")
            .label("c")
            .exec().callOnThis("state").literal("c").endCall().endExec()
            .gotoLabel("b")
            .label("end")
          .endMethod()
        .endClass();

//    new TypePrinter().print(testClass);

    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(testClass);
    Class<?> generated = (Class<?>)cl.loadClass("brennus.asm.TestGoto$TestClass");

    FSA fsa = (FSA)generated.newInstance();
    fsa.exec();
    assertEquals(Arrays.asList("a", "c", "b"), fsa.getStates());
  }
}
