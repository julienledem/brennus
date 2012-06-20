package brennus.example;

import static brennus.ClassBuilder.*;
import static brennus.Protection.*;
import static brennus.Type.*;
import brennus.ClassBuilder;
import brennus.MethodBuilder;
import brennus.SwitchBuilder;
import brennus.Type;
import brennus.printer.TypePrinter;


import org.junit.Test;


public class TestClassBuilder {

  @Test
  public void testBuilder() {
    Type testClass =
        startClass("test.TestClass").extending(existing(TestClassBuilder.class))
          .field(STRING, "foo", Private)
          .field(INT, "bar", Private)
          .startMethod(STRING, "getFoo", Public)
            .expression().get("foo").returnExp()
          .endMethod()
          .startMethod(INT, "getBar", Public)
            .expression().get("bar").returnExp()
          .endMethod()
          .startMethod(OBJECT, "get", Public)
            .withParameter(INT, "i")
            .expression().get("i").switchOnExp()
              .caseBlock(0)
                .expression().call("getFoo").returnExp()
              .endCase()
              .caseBlock(1)
                .expression().call("getBar").returnExp()
              .endCase()
              .defaultCase()
                .expression().call("error").done()
              .endCase()
            .endSwitch()
          .endMethod()
        .endClass();
    new TypePrinter().print(testClass);
  }

  public void error() {
    throw new RuntimeException();
  }

  @Test
  public void testBuilder2() {
    ClassBuilder classBuilder = startClass("test.TestClass").extending(existing(TestClassBuilder.class));

    classBuilder.field(STRING, "foo", Private);
    classBuilder.field(INT, "bar", Private);

    MethodBuilder methodBuilder = classBuilder.startMethod(STRING, "getFoo", Public);
    methodBuilder.expression().get("foo").returnExp().endMethod();

    methodBuilder = classBuilder.startMethod(INT, "getBar", Public);
    methodBuilder.expression().get("bar").returnExp().endMethod();

    methodBuilder = classBuilder.startMethod(OBJECT, "get", Public).withParameter(INT, "i");
    SwitchBuilder<MethodBuilder> switchBuilder = methodBuilder.expression().get("i").switchOnExp();
    switchBuilder.caseBlock(0).expression().call("getFoo").returnExp().endCase();
    switchBuilder.caseBlock(1).expression().call("getBar").returnExp().endCase();
    switchBuilder.defaultCase().expression().call("error").done().endCase();
    switchBuilder.endSwitch();
    methodBuilder.endMethod();


    Type testClass = classBuilder.endClass();
    new TypePrinter().print(testClass);
  }

}
