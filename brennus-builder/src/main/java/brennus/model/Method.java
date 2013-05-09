package brennus.model;

import brennus.ImmutableList;

public final class Method {

  private final String typeName;
  private final MemberFlags memberFlags;
  private final Type returnType;
  private final String name;
  private final ImmutableList<Parameter> parameters;
  private final ImmutableList<Statement> statements;
  private final boolean interfaceMethod;

  public Method(String typeName, MemberFlags memberFlags, Type returnType, String name, ImmutableList<Parameter> parameters, ImmutableList<Statement> statements, boolean interfaceMethod) {
    this.typeName = typeName;
    this.memberFlags = memberFlags;
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
    this.statements = statements;
    this.interfaceMethod = interfaceMethod;
  }

  public String getName() {
    return name;
  }

  public Type getReturnType() {
    return returnType;
  }

  public ImmutableList<Statement> getStatements() {
    return statements;
  }

  public boolean isStatic() {
    return memberFlags.isStatic();
  }

  public MemberFlags getFlags() {
    return memberFlags;
  }

  public ImmutableList<Parameter> getParameters() {
    return parameters;
  }

  public String getSignature() {
    StringBuilder signature = new StringBuilder("(");

    for (Parameter parameter : parameters) {
      signature.append(parameter.getType().getSignature());
    }

    signature.append(")");

    signature.append(returnType.getSignature());
    return signature.toString();
  }

  public String getTypeName() {
    return typeName;
  }

  public boolean isInterfaceMethod() {
    return interfaceMethod;
  }

  @Override
  public String toString() {
    return "["+getClass().getSimpleName()+" "+name+getSignature()+"]";
  }

}
