package brennus.model;

public class TypeIdentifier {
  
  public static ArrayTypeIdentifier arrayIdentifier(Class<?> componentType) {
    return new ArrayTypeIdentifier(identifier(componentType));
  }
    
  public static TypeIdentifier identifier(Class<?> type) {
    if (type.isArray()) {
      return arrayIdentifier(type);
    }
    return classIdentifier(type);
  }

  private static TypeIdentifier classIdentifier(Class<?> type) {
    String className = type.getName();
    String identifier = className.replace(".", "/");
    String signature = "L" + identifier + ";";
    return new ClassTypeIdentifier(className, signature);
  }
  
  private final String signature;
  
  
  TypeIdentifier(String signature) {
    super();
    this.signature = signature;
  }
  
  @Override
  public String toString() {
    return signature;
  }
  
  public static class ArrayTypeIdentifier extends TypeIdentifier {
    private final TypeIdentifier component;
    
    private ArrayTypeIdentifier(TypeIdentifier component) {
      super("[" + component);
      this.component = component;
    }
  }
  
  public static class ClassTypeIdentifier extends TypeIdentifier {
    
    private String className;

    private ClassTypeIdentifier(String className, String signature) {
      super(signature);
      this.className = className;
    }
  }
}
