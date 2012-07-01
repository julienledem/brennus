package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.MethodBuilder.MethodHandler;
import brennus.model.ExistingType;
import brennus.model.Field;
import brennus.model.FutureType;
import brennus.model.Keyword;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Type;

/**
 * builds a class
 *
 * @author Julien Le Dem
 *
 */
public class ClassBuilder {

  private final String name;
  private Type extending;
  private final List<Field> fields = new ArrayList<Field>();
  private final List<Field> staticFields = new ArrayList<Field>();
  private final List<Method> methods = new ArrayList<Method>();
  private final List<Method> staticMethods = new ArrayList<Method>();

  private ClassBuilder(String name) {
    this.name = name;
  }

  // builder methods

  public static ClassBuilder startClass(String name) {
    return new ClassBuilder(name);
  }

  public ClassBuilder extendsType(Type type) {
    if (extending != null) {
      throw new IllegalStateException("Can't extend "+type+" Allready extending "+extending);
    }
    this.extending = type;
    return this;
  }

  public ClassBuilder field(Type type, String name, Keyword... keywords) {
    Field f = new Field(new MemberFlags(keywords), type, name);
    (f.isStatic() ? staticFields : fields).add(f);
    return this;
  }

  public MethodDeclarationBuilder startMethod(Type returnType, String methodName, Keyword... keywords) {
    return new MethodDeclarationBuilder(this.name.replace(".", "/"), new MemberFlags(keywords), returnType, methodName, new MethodHandler() {
      public ClassBuilder handleMethod(Method method) {
        addMethod(method);
        return ClassBuilder.this;
      }
    });
  }

  public FutureType endClass() {
    FutureType futureType = new FutureType(getName(), extending == null ? ExistingType.OBJECT : extending, fields, staticFields, methods, staticMethods);
    new ClassValidator().validate(futureType);
    return futureType;
  }

  // internals

  private void addMethod(Method method) {
    (method.isStatic() ? staticMethods : methods).add(method);
  }

  public String getName() {
    return name;
  }
}
