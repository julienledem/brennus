package brennus.asm;

import static brennus.ClassBuilder.startClass;
import static brennus.model.ExistingType.INT;
import static brennus.model.ExistingType.STRING;
import static brennus.model.ExistingType.VOID;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import brennus.asm.TestGeneration.DynamicClassLoader;
import brennus.model.FutureType;
import brennus.printer.TypePrinter;

public class TestGetSet {

  abstract public static class Base {

    List<String> toValidate = new ArrayList<String>();

    public void validate(Object value) {
      toValidate.add(String.valueOf(value));
    }

    abstract public void method(String stringParam, int intParam);

  }

  @Test
  public void testGetSet() throws Exception {
    FutureType testClass =
        startClass("brennus.asm.TestGetSet$TestClass", existing(Base.class))
          .field(PUBLIC, STRING, "stringField")
          .field(PUBLIC, INT, "intField")
          .startMethod(PUBLIC, VOID, "method").param(STRING, "stringParam").param(INT, "intParam")
            .var(STRING, "stringVar")
            .var(INT, "intVar")
            .set("stringField").literal("stringFieldSet").endSet()
            .set("intField").literal(1).endSet()
            .set("stringVar").literal("stringVarSet").endSet()
            .set("intVar").literal(4).endSet()
            .exec().callOnThis("validate").get("intField").endCall().endExec()
            .exec().callOnThis("validate").get("stringField").endCall().endExec()
            .exec().callOnThis("validate").get("intParam").endCall().endExec()
            .exec().callOnThis("validate").get("stringParam").endCall().endExec()
            .exec().callOnThis("validate").get("intVar").endCall().endExec()
            .exec().callOnThis("validate").get("stringVar").endCall().endExec()
            .set("stringField").literal("stringFieldSet2").endSet()
            .set("intField").literal(2).endSet()
            .set("stringParam").literal("stringParamSet").endSet()
            .set("intParam").literal(3).endSet()
            .set("stringVar").literal("stringVarSet2").endSet()
            .set("intVar").literal(5).endSet()
            .exec().callOnThis("validate").get("intField").endCall().endExec()
            .exec().callOnThis("validate").get("stringField").endCall().endExec()
            .exec().callOnThis("validate").get("intParam").endCall().endExec()
            .exec().callOnThis("validate").get("stringParam").endCall().endExec()
            .exec().callOnThis("validate").get("intVar").endCall().endExec()
            .exec().callOnThis("validate").get("stringVar").endCall().endExec()
          .endMethod()
        .endClass();
    new TypePrinter().print(testClass);
    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(testClass);
    Class<?> generated = (Class<?>)cl.loadClass("brennus.asm.TestGetSet$TestClass");
    Base base = (Base)generated.newInstance();
    base.method("p1", 6);
    Assert.assertEquals(Arrays.asList(
          "1", "stringFieldSet", "6", "p1", "4", "stringVarSet","2", "stringFieldSet2", "3", "stringParamSet", "5", "stringVarSet2"
        ), base.toValidate);
  }


}
