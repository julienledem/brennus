package brennus;

import java.util.List;

public class Method {

  private final MemberFlags memberFlags;
  private final Type returnType;
  private final String name;
  private final List<Parameter> parameters;
  private final List<Statement> statements;

  Method(MemberFlags memberFlags, Type returnType, String name, List<Parameter> parameters, List<Statement> statements) {
    this.memberFlags = memberFlags;
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
    this.statements = statements;
  }

  public String getName() {
    return name;
  }

  public Type getReturnType() {
    return returnType;
  }

  public List<Statement> getStatements() {
    return statements;
  }

  public boolean isStatic() {
    return memberFlags.isStatic();
  }

  public MemberFlags getFlags() {
    return memberFlags;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

}
