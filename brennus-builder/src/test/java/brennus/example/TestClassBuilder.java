package brennus.example;

import static brennus.ClassBuilder.*;
import static brennus.model.ExistingType.*;
import static brennus.model.Protection.*;
import brennus.ClassBuilder;
import brennus.MethodBuilder;
import brennus.SwitchBuilder;
import brennus.model.Type;
import brennus.printer.TypePrinter;


import org.junit.Test;


public class TestClassBuilder {

  @Test
  public void testBuilder() {
    Type testClass =
        startClass("test.TestClass").extendsType(existing(TestClassBuilder.class))
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
  }

  @Test
  public void testBuilder2() {
    ClassBuilder classBuilder = startClass("test.TestClass").extendsType(existing(TestClassBuilder.class));

    classBuilder.field(STRING, "foo", PRIVATE);
    classBuilder.field(INT, "bar", PRIVATE);

    MethodBuilder methodBuilder = classBuilder.startMethod(STRING, "getFoo", PUBLIC);
    methodBuilder.expression().get("foo").returnExp().endMethod();

    methodBuilder = classBuilder.startMethod(INT, "getBar", PUBLIC);
    methodBuilder.expression().get("bar").returnExp().endMethod();

    methodBuilder = classBuilder.startMethod(OBJECT, "get", PUBLIC).withParameter(INT, "i");
    SwitchBuilder<MethodBuilder> switchBuilder = methodBuilder.expression().get("i").switchOn();
    switchBuilder.caseBlock(0).expression().call("getFoo").returnExp().endCase();
    switchBuilder.caseBlock(1).expression().call("getBar").returnExp().endCase();
    switchBuilder.defaultCase().expression().call("error").throwException().endCase();
    switchBuilder.endSwitch();
    methodBuilder.endMethod();


    Type testClass = classBuilder.endClass();
    new TypePrinter().print(testClass);
  }

}
