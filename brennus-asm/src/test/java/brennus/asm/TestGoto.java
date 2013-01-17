package brennus.asm;

import static brennus.model.ExistingType.VOID;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import brennus.Builder;
import brennus.MethodBuilder;
import brennus.SwitchBuilder;
import brennus.ThenBuilder;
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

  abstract public static class FSA2 {

    private List<String> states = new ArrayList<String>();

    abstract public void exec(Iterator<Integer> it);

    public void state(String p) {
      states.add(p);
    }

    public List<String> getStates() {
      return states;
    }
  }

  @Test
  public void testGoto() throws Exception {

    FutureType testClass = new Builder()
        .startClass("brennus.asm.TestGoto$TestClass", existing(FSA.class))
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

  @Test
  public void testFSA() throws Exception {
    int[][] fsa = {
        {0,1,2,3},
        {0,1,2,3},
        {0,1,2,3},
        {0,1,2,3}
    };

    MethodBuilder m = new Builder()
      .startClass("brennus.asm.TestGoto$TestClass2", existing(FSA2.class))
          .startMethod(PUBLIC, VOID, "exec").param(existing(Iterator.class), "it")
          .gotoLabel("start")
          .label("start");
    for (int i = 0; i < fsa.length; i++) {
      m = m.label("s_"+i)
          .exec().callOnThis("state").literal("s_"+i).endCall().endExec();
      SwitchBuilder<ThenBuilder<MethodBuilder>> s = m.ifExp().get("it").callNoParam("hasNext").thenBlock()
             .switchOn().get("it").callNoParam("next").switchBlock();
      for (int j = 0; j < fsa[i].length; j++) {
        int to = fsa[i][j];
        s = s.caseBlock(j)
              .gotoLabel("s_"+to)
            .endCase();
      }
      m = s.endSwitch()
          .elseBlock()
            .gotoLabel("end")
          .endIf();
    }
    FutureType testClass = m.label("end").endMethod().endClass();

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
    Class<?> generated = (Class<?>)cl.loadClass("brennus.asm.TestGoto$TestClass2");

    FSA2 compiledFSA = (FSA2)generated.newInstance();
    compiledFSA.exec(Arrays.asList(3,2,1).iterator());
    assertEquals(Arrays.asList("s_0", "s_3", "s_2", "s_1"), compiledFSA.getStates());
  }
}
