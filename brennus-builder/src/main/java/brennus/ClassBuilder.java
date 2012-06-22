package brennus;

import java.util.ArrayList;
import java.util.List;

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
  private List<Field> fields = new ArrayList<Field>();
  private List<Field> staticFields = new ArrayList<Field>();
  private List<Method> methods = new ArrayList<Method>();
  private List<Method> staticMethods = new ArrayList<Method>();

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

  public MethodBuilder startMethod(Type returnType, String name, Keyword... keywords) {
    return new MethodBuilder(this, new MemberFlags(keywords), returnType, name);
  }

  public FutureType endClass() {
    return new FutureType(getName(), extending == null ? ExistingType.OBJECT : extending, fields, staticFields, methods, staticMethods);
  }

  // internals

  void addMethod(Method method) {
    (method.isStatic() ? staticMethods : methods).add(method);
  }

  public String getName() {
    return name;
  }
}
