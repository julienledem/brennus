package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExistingType extends Type {

  private static final Map<Class<?>, PrimitiveType> primitives = new HashMap<Class<?>, PrimitiveType>();

  public static final PrimitiveType VOID = primitive(Void.TYPE, "V", Void.class);
  public static final PrimitiveType INT = primitive(Integer.TYPE, "I", Integer.class);
  public static final PrimitiveType LONG = primitive(Long.TYPE, "J", Long.class);
  public static final PrimitiveType BYTE = primitive(Byte.TYPE, "B", Byte.class);
  public static final PrimitiveType BOOLEAN = primitive(Boolean.TYPE, "Z", Boolean.class);
  public static final PrimitiveType SHORT = primitive(Short.TYPE, "S", Short.class);
  public static final PrimitiveType CHAR = primitive(Character.TYPE, "C", Character.class);
  public static final PrimitiveType FLOAT = primitive(Float.TYPE, "F", Float.class);
  public static final PrimitiveType DOUBLE = primitive(Double.TYPE, "D", Double.class);

  public static final ExistingType STRING = existing(String.class);
  public static final ExistingType OBJECT = existing(Object.class);


  private static PrimitiveType primitive(Class<?> existingPrimitive, String identifier, Class<?> boxedType) {
    if (!existingPrimitive.isPrimitive()) {
      throw new RuntimeException("Not a primitive " + existingPrimitive);
    }
    PrimitiveType existingType = new PrimitiveType(existingPrimitive, identifier, identifier, existing(boxedType));
    primitives.put(existingPrimitive, existingType);
    return existingType;
  }

  public static ExistingType existing(Class<?> existing) {
    if (existing.isPrimitive()) {
      ExistingType existingType = primitives.get(existing);
      if (existingType == null) {
        throw new RuntimeException("Unexpected primitive " + existing);
      }
      return existingType;
    }
    String identifier;
    String signature;
    if (existing.isArray()) {
      ExistingType component = existing(existing.getComponentType());
      identifier = "[" + component.getClassIdentifier();
      signature = identifier;
    } else {
      identifier = existing.getName().replace(".", "/");
      signature = "L" + identifier + ";";
    }
    return new ExistingType(existing, identifier, signature);
  }

  private final Class<?> existing;
  private final String identifier;
  private final String signature;

  ExistingType(Class<?> existing, String identifier, String signature) {
    this.existing = existing;
    this.identifier = identifier;
    this.signature = signature;
  }

  @Override
  public void accept(TypeVisitor typeVisitor) {
    wrap(typeVisitor).visit(this);
  }

  public Class<?> getExisting() {
    return existing;
  }

  @Override
  public String getName() {
    return existing.getName();
  }

  @Override
  public String getSignature() {
    return signature;
  }

  public Class<?> getExistingClass() {
    return existing;
  }

  @Override
  public String getClassIdentifier() {
    return identifier;
  }

  @Override
  public boolean isPrimitive() {
    return existing.isPrimitive();
  }

  @Override
  public Method getMethod(String methodName, int parameterCount) {
    // TODO: add static
    java.lang.reflect.Method[] declaredMethods = existing.getDeclaredMethods();
    for (java.lang.reflect.Method method : declaredMethods) {
      Class<?>[] parameterTypes = method.getParameterTypes();
      if (method.getName().equals(methodName) && parameterTypes.length == parameterCount) {
        ArrayList<Parameter> parameters = convertParameters(parameterTypes);
        return new Method(
            this.getClassIdentifier(),
            MemberFlags.fromReflection(method),
            existing(method.getReturnType()),
            methodName,
            parameters,
            new ArrayList<Statement>());
      }
    }
    return null;
  }

  private ArrayList<Parameter> convertParameters(Class<?>[] parameterTypes) {
    ArrayList<Parameter> parameters = new ArrayList<Parameter>();
    for (int i = 0; i < parameterTypes.length; i++) {
      Class<?> paramClass = parameterTypes[i];
      parameters.add(
          new Parameter(
              ExistingType.existing(paramClass),
              "arg"+i,
              i));

    }
    return parameters;
  }

  @Override
  public Method getConstructor(int parameterCount) {
    for (java.lang.reflect.Constructor<?> constructor : existing.getConstructors()) {
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      if (parameterTypes.length == parameterCount) {
        ArrayList<Parameter> parameters = convertParameters(parameterTypes);
        return new Method(
            this.getClassIdentifier(),
            MemberFlags.fromReflection(constructor),
            VOID,
            "<init>",
            parameters,
            new ArrayList<Statement>());
      }
    }
    return null;
  }

  @Override
  public boolean isAssignableFrom(Type type) {
//    System.out.println(this+".isAssignableFrom."+type);
    class TypeVisitorImplementation implements TypeVisitor {
      boolean isAssignableFrom;
      public void visit(ExistingType other) {
        isAssignableFrom = existing.isAssignableFrom(other.existing);
      }
      public void visit(FutureType futureType) {
        isAssignableFrom = isAssignableFrom(futureType.getExtending());
      }
    }
    TypeVisitorImplementation typeVisitor = new TypeVisitorImplementation();
    type.accept(typeVisitor);
//    System.out.println(this+".isAssignableFrom."+type+"="+typeVisitor.isAssignableFrom);
    return typeVisitor.isAssignableFrom;
  }

}
