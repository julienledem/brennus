package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

import java.util.Collections;
import java.util.List;

public final class FutureType extends Type {

  private final String name;
  private final Type extending;
  private final List<Field> fields;
  private final List<Field> staticFields;
  private final List<Method> methods;
  private final List<Method> staticMethods;
  private final String sourceFile;
  private final List<Method> constructors;

  public FutureType(String name, Type extending, List<Field> fields, List<Field> staticFields, List<Method> methods, List<Method> staticMethods, List<Method> constructors, String sourceFile) {
    this.name = name;
    this.extending = extending;
    this.fields = Collections.unmodifiableList(fields);
    this.staticFields = Collections.unmodifiableList(staticFields);
    this.methods = Collections.unmodifiableList(methods);
    this.staticMethods = Collections.unmodifiableList(staticMethods);
    this.constructors = Collections.unmodifiableList(constructors);
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

  public List<Field> getFields() {
    return fields;
  }

  public List<Field> getStaticFields() {
    return staticFields;
  }

  public List<Method> getMethods() {
    return methods;
  }

  public List<Method> getStaticMethods() {
    return staticMethods;
  }

  public List<Method> getConstructors() {
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
    if (extending!=null) {
      return extending.getMethod(methodName, parameterCount);
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

}
