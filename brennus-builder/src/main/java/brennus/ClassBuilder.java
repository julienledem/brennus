package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.MethodBuilder.MethodHandler;
import brennus.model.ExistingType;
import brennus.model.Field;
import brennus.model.FutureType;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Protection;
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
  private final String sourceFile;

  private ClassBuilder(String name, Type extending) {
    this.name = name;
    this.extending = extending;
    StackTraceElement creatingStackFrame = MethodContext.getCreatingStackFrame();
    if (creatingStackFrame == null) {
      sourceFile = "generated";
    } else {
      sourceFile = creatingStackFrame.getFileName();
    }
  }

  // builder methods

  /**
   * startClass(name, extending)[.[static]field(protection, type, name)]*[.start[Static]Method(protection, return, name){statements}.endMethod()]*.endClass()
   * @param name the fully qualified name of the class
   * @return a ClassBuilder
   */
  public static ClassBuilder startClass(String name, Type extending) {
    return new ClassBuilder(name, extending);
  }

  /**
   * startClass(name, extending)[.[static]field(protection, type, name)]*[.start[Static]Method(protection, return, name){statements}.endMethod()]*.endClass()
   * @param name the fully qualified name of the class
   * @return a ClassBuilder
   */
  public static ClassBuilder startClass(String name) {
    return new ClassBuilder(name, ExistingType.OBJECT);
  }

  /**
   * @param protection public/package/protected/private
   * @param type
   * @param name
   * @return this
   */
  public ClassBuilder field(Protection protection, Type type, String name) {
    field(protection, type, name, false);
    return this;
  }

  /**
   * @param protection public/package/protected/private
   * @param type
   * @param name
   * @return this
   */
  public ClassBuilder staticField(Protection protection, Type type, String name) {
    field(protection, type, name, true);
    return this;
  }

  /**
   * .startStaticMethod(protection, return, name){statements}.endMethod()
   *
   * @param protection public/package/protected/private
   * @param returnType
   * @param methodName
   * @return a MethodDeclarationBuilder
   */
  public MethodDeclarationBuilder startStaticMethod(Protection protection, Type returnType, String methodName) {
    return startMethod(protection, returnType, methodName, true);
  }

  /**
   * .startMethod(protection, return, name){statements}.endMethod()
   *
   * @param protection public/package/protected/private
   * @param returnType
   * @param methodName
   * @return a MethodDeclarationBuilder
   */
  public MethodDeclarationBuilder startMethod(Protection protection, Type returnType, String methodName) {
    return startMethod(protection, returnType, methodName, false);
  }

  /**
   * @return the resulting class
   */
  public FutureType endClass() {
    FutureType futureType = new FutureType(name, extending == null ? ExistingType.OBJECT : extending, fields, staticFields, methods, staticMethods, sourceFile);
    new ClassValidator().validate(futureType);
    return futureType;
  }

  // internals

  private MethodDeclarationBuilder startMethod(Protection protection, Type returnType, String methodName, final boolean isStatic) {
    return new MethodDeclarationBuilder(this.name.replace(".", "/"), new MemberFlags(isStatic, protection), returnType, methodName, new MethodHandler() {
      public ClassBuilder handleMethod(Method method) {
        (isStatic ? staticMethods : methods).add(method);
        return ClassBuilder.this;
      }
    });
  }

  private void field(Protection protection, Type type, String name, boolean isStatic) {
    (isStatic ? staticFields : fields).add(new Field(new MemberFlags(isStatic, protection), type, name));
  }

}
