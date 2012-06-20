package brennus;

import java.util.ArrayList;
import java.util.List;

public class MethodBuilder extends CodeBlockBuilder<Statement> {

  private final ClassBuilder classBuilder;
  private final MemberFlags memberFlags;
  private final Type returnType;
  private final String name;
  private final List<Parameter> parameters = new ArrayList<Parameter>();


  MethodBuilder(ClassBuilder classBuilder, MemberFlags memberFlags, Type returnType, String name) {
    this.classBuilder = classBuilder;
    this.memberFlags = memberFlags;
    this.returnType = returnType;
    this.name = name;
  }

  public ExpressionBuilder<MethodBuilder> expression() {
    return new ExpressionBuilder<MethodBuilder>(this);
  }

  public ClassBuilder endMethod() {
    classBuilder.addMethod(new Method(memberFlags, returnType, name, parameters, getStatements()));
    return classBuilder;
  }

  public MethodBuilder withParameter(Type type, String name) {
    parameters.add(new Parameter(type, name));
    return this;
  }

}
