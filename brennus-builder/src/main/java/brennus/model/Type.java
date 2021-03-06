package brennus.model;


abstract public class Type {

  abstract public void accept(TypeVisitor typeVisitor);

  abstract public String getName();

  abstract public String getPrintableName();

  abstract public String getClassIdentifier();

  abstract public String getSignature();

  abstract public boolean isPrimitive();

  abstract public Method getMethod(String methodName, int parameterCount);

  abstract public Field getField(String varName);

  abstract public boolean isAssignableFrom(Type type);

  abstract public Method getConstructor(int parameterCount);

  abstract public Type unNestArray();

  abstract public Type nestArray();

  @Override
  public String toString() {
    return "["+getClass().getSimpleName()+" "+getName()+"]";
  }


}
