package brennus.asm;

import static brennus.model.ExistingType.BOOLEAN;
import static brennus.model.ExistingType.INT;
import static brennus.model.ExistingType.STRING;
import static brennus.model.ExistingType.VOID;
import static brennus.model.ExistingType.existing;
import static brennus.model.Protection.PUBLIC;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import brennus.Builder;
import brennus.asm.TestGeneration.DynamicClassLoader;
import brennus.model.FutureType;
import brennus.printer.TypePrinter;

import org.junit.Test;

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
    FutureType testClass = new Builder()
        .startClass("brennus.asm.TestGetSet$TestClass", existing(Base.class))
          .field(PUBLIC, STRING, "stringField")
          .field(PUBLIC, INT, "intField")
          .field(PUBLIC, BOOLEAN, "boolField")
          .staticField(PUBLIC,STRING, "staticStringField")
          .startMethod(PUBLIC, VOID, "method").param(STRING, "stringParam").param(INT, "intParam")
            .var(STRING, "stringVar")
            .var(INT, "intVar")
            .var(BOOLEAN, "boolVar")
            .set("intField").literal(1).endSet()
            .set("stringField").literal("stringFieldSet").endSet()
            .set("staticStringField").literal("staticStringField").endSet()
            .set("stringVar").literal("stringVarSet").endSet()
            .set("intVar").literal(4).endSet()
            .set("boolVar").get("stringParam").isNull().endSet()
            .exec().callOnThis("validate").get("intField").endCall().endExec() // 1
            .exec().callOnThis("validate").get("stringField").endCall().endExec()  // stringFieldSet
            .exec().callOnThis("validate").get("staticStringField").endCall().endExec()  // staticStringField
            .exec().callOnThis("validate").get("intParam").endCall().endExec() // param: 6 then 3
            .exec().callOnThis("validate").get("stringParam").endCall().endExec() // param: p1 then null
            .exec().callOnThis("validate").get("intVar").endCall().endExec() // 4
            .exec().callOnThis("validate").get("stringVar").endCall().endExec() // stringVarSet
            .exec().callOnThis("validate").get("boolField").endCall().endExec() // false by default then true
            .exec().callOnThis("validate").get("boolVar").endCall().endExec()   // param: false then true
            .set("stringField").literal("stringFieldSet2").endSet()
            .set("intField").literal(2).endSet()
            .set("stringParam").literal("stringParamSet").endSet()
            .set("intParam").literal(3).endSet()
            .set("stringVar").literal("stringVarSet2").endSet()
            .set("intVar").literal(5).endSet()
            .set("boolField").literal(true).endSet()
            .set("boolVar").literal(false).endSet()
            .exec().callOnThis("validate").get("intField").endCall().endExec() // 2
            .exec().callOnThis("validate").get("stringField").endCall().endExec() // stringFieldSet2
            .exec().callOnThis("validate").get("intParam").endCall().endExec() // 3
            .exec().callOnThis("validate").get("stringParam").endCall().endExec() // stringParamSet
            .exec().callOnThis("validate").get("intVar").endCall().endExec() // 5
            .exec().callOnThis("validate").get("stringVar").endCall().endExec() // stringVarSet2
            .exec().callOnThis("validate").get("boolField").endCall().endExec() // true
            .exec().callOnThis("validate").get("boolVar").endCall().endExec() // false
          .endMethod()
        .endClass();
    new TypePrinter().print(testClass);
    DynamicClassLoader cl = new DynamicClassLoader();
    cl.define(testClass);
    Class<?> generated = (Class<?>)cl.loadClass("brennus.asm.TestGetSet$TestClass");
    Base base = (Base)generated.newInstance();
    base.method("p1", 6);
    base.method(null, 3);
    String foo = null;
    boolean bar = foo == null;
    assertTrue(bar);
    assertEquals(Arrays.asList(
          "1", "stringFieldSet", "staticStringField", "6", "p1", "4", "stringVarSet", "false", "false",
            "2", "stringFieldSet2", "3", "stringParamSet", "5", "stringVarSet2", "true", "false",
          "1", "stringFieldSet", "staticStringField", "3", "null", "4", "stringVarSet", "true", "true",
            "2", "stringFieldSet2", "3", "stringParamSet", "5", "stringVarSet2", "true", "false"
        ).toString(), base.toValidate.toString());
  }


}
