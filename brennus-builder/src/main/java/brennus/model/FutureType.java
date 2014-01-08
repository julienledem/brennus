package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;
import brennus.ImmutableList;

public final class FutureType extends Type {

  private final String name;
  private final Type extending;
  private final ImmutableList<Field> fields;
  private final ImmutableList<Field> staticFields;
  private final ImmutableList<Method> methods;
  private final ImmutableList<Method> staticMethods;
  private final String sourceFile;
  private final ImmutableList<Method> constructors;

  public FutureType(String name, Type extending, ImmutableList<Field> fields, ImmutableList<Field> staticFields, ImmutableList<Method> methods, ImmutableList<Method> staticMethods, ImmutableList<Method> constructors, String sourceFile) {
    this.name = name;
    this.extending = extending;
    this.fields = fields;
    this.staticFields = staticFields;
    this.methods = methods;
    this.staticMethods = staticMethods;
    this.constructors = constructors;
    this.sourceFile = sourceFile;
  }

  @Override
  public void accept(TypeVisitor typeVisitor) {
    wrap(typeVisitor).visit(this);
  }

  public String getName() {
    return name;
  }

  public Type getExtending() {
    return extending;
  }

  public ImmutableList<Field> getFields() {
    return fields;
  }

  public ImmutableList<Field> getStaticFields() {
    return staticFields;
  }

  public ImmutableList<Method> getMethods() {
    return methods;
  }

  public ImmutableList<Method> getStaticMethods() {
    return staticMethods;
  }

  public ImmutableList<Method> getConstructors() {
    return constructors;
  }

  @Override
  public String getClassIdentifier() {
    return getName().replace('.', '/');
  }

  @Override
  public String getSignature() {
    return "L" + getClassIdentifier() + ";";
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }
// TODO add parameters
  // TODO add static methods
  @Override
  public Method getMethod(String methodName, int parameterCount) {
    for (Method method : methods) {
      if (method.getName().equals(methodName) && method.getParameters().size() == parameterCount) {
        return method;
      }
    }
    if (extending != null) {
      return extending.getMethod(methodName, parameterCount);
    }
    return null;
  }

  @Override
  public Field getField(String varName) {
    Field field = getField(getFields(), varName);
    if (field == null) {
      field = getField(getStaticFields(), varName);
    }
    if (field == null && extending != null) {
      field = extending.getField(varName);
    }
    return field;
  }

  private Field getField(ImmutableList<Field> fields, String varName) {
    for (Field field : fields) {
      if (field.getName().equals(varName)) {
        return field;
      }
    }
    return null;
  }


  @Override
  public boolean isAssignableFrom(Type type) {
    System.out.println(this+".isAssignableFrom."+type);
    class TypeVisitorImplementation implements TypeVisitor {
      boolean isAssignableFrom;
      public void visit(ExistingType other) {
        isAssignableFrom = false;
      }
      public void visit(FutureType futureType) {
        isAssignableFrom = name.equals(futureType.name) || isAssignableFrom(futureType.getExtending());
      }
    }
    TypeVisitorImplementation typeVisitor = new TypeVisitorImplementation();
    type.accept(typeVisitor);
    System.out.println(this+".isAssignableFrom."+type+"="+typeVisitor.isAssignableFrom);
    return typeVisitor.isAssignableFrom;
  }

  public String getSourceFile() {
    return sourceFile;
  }

  public Method getSuperConstructor(int parameterCount) {
    return getExtending().getConstructor(parameterCount);
  }

  @Override
  public Method getConstructor(int parameterCount) {
    for (Method constructor : constructors) {
      if (constructor.getParameters().size() == parameterCount) {
        return constructor;
      }
    }
    if (extending!=null) {
      return extending.getConstructor(parameterCount);
    }
    return null;
  }

  @Override
  public Type unNestArray() {
    throw new RuntimeException("not an array type: " + this);
  }

  @Override
  public Type nestArray() {
    throw new UnsupportedOperationException("NYI");
  }
}
