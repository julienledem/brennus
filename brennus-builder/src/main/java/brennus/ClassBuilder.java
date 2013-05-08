package brennus;

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
public final class ClassBuilder {

  private final String name;
  private final Type extending;
  private final ImmutableList<Field> fields;
  private final ImmutableList<Field> staticFields;
  private final ImmutableList<Method> methods;
  private final ImmutableList<Method> staticMethods;
  private final ImmutableList<Method> constructors;
  private final String sourceFile;
  private final Builder builder;

  ClassBuilder(String name, Type extending, Builder builder) {
    this(
        name,
        extending,
        ImmutableList.<Field>empty(),
        ImmutableList.<Field>empty(),
        ImmutableList.<Method>empty(),
        ImmutableList.<Method>empty(),
        ImmutableList.<Method>empty(),
        getSourceFile(builder.getCreatingStackFrame()),
        builder
        );
  }

  private static String getSourceFile(StackTraceElement creatingStackFrame) {
    if (creatingStackFrame == null) {
      return "generated";
    } else {
      return creatingStackFrame.getFileName();
    }
  }

  private ClassBuilder(
      String name,
      Type extending,
      ImmutableList<Field> fields,
      ImmutableList<Field> staticFields,
      ImmutableList<Method> methods,
      ImmutableList<Method> staticMethods,
      ImmutableList<Method> constructors,
      String sourceFile,
      Builder builder) {
    super();
    this.name = name;
    this.extending = extending;
    this.fields = fields;
    this.staticFields = staticFields;
    this.methods = methods;
    this.staticMethods = staticMethods;
    this.constructors = constructors;
    this.sourceFile = sourceFile;
    this.builder = builder;
  }

  // builder methods

  /**
   * @param protection public/package/protected/private
   * @param type
   * @param name
   * @return this
   */
  public ClassBuilder field(Protection protection, Type type, String name) {
    return addField(protection, type, name, false);
  }

  /**
   * @param protection public/package/protected/private
   * @param type
   * @param name
   * @return this
   */
  public ClassBuilder staticField(Protection protection, Type type, String name) {
    return addField(protection, type, name, true);
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
    FutureType futureType = new FutureType(name, extending == null ? ExistingType.OBJECT : extending, fields, staticFields, methods, staticMethods, constructors, sourceFile);
    new ClassValidator().validate(futureType);
    return futureType;
  }


  public ConstructorDeclarationBuilder startConstructor(Protection protection) {
    return new ConstructorDeclarationBuilder(this.name.replace(".", "/"), protection, new MethodHandler() {
      public ClassBuilder handleMethod(Method method) {
        return addConstructor(method);
      }
    }, builder);
  }

  // internals

  private MethodDeclarationBuilder startMethod(Protection protection, Type returnType, String methodName, final boolean isStatic) {
    // TODO: allow final
    return new MethodDeclarationBuilder(this.name.replace(".", "/"), new MemberFlags(isStatic, false, protection), returnType, methodName, new MethodHandler() {
      public ClassBuilder handleMethod(Method method) {
        return addMethod(isStatic, method);
      }
    }, builder);
  }

  private ClassBuilder newClassBuilder(
      ImmutableList<Field> newFields,
      ImmutableList<Field> newStaticFields,
      ImmutableList<Method> newMethods,
      ImmutableList<Method> newStaticMethods,
      ImmutableList<Method> newConstructors) {
    return new ClassBuilder(
        this.name,
        this.extending,
        newFields,
        newStaticFields,
        newMethods,
        newStaticMethods,
        newConstructors,
        this.sourceFile,
        this.builder);

  }

  private ClassBuilder addField(Protection protection, Type type, String name, boolean isStatic) {
    ImmutableList<Field> newFields = this.fields;
    ImmutableList<Field> newStaticFields = this.staticFields;
    final Field newField = new Field(new MemberFlags(isStatic, false, protection), type, name);
    if (isStatic) {
      newStaticFields = newStaticFields.append(newField);
    } else {
      newFields = newFields.append(newField);
    }
    return newClassBuilder(
        newFields,
        newStaticFields,
        this.methods,
        this.staticMethods,
        this.constructors);
  }

  private ClassBuilder addConstructor(Method constructor) {
    return newClassBuilder(
        this.fields,
        this.staticFields,
        this.methods,
        this.staticMethods,
        this.constructors.append(constructor));
  }

  private ClassBuilder addMethod(final boolean isStatic, Method method) {
    ImmutableList<Method> newMethods = this.methods;
    ImmutableList<Method> newStaticMethods = this.staticMethods;
    if (isStatic) {
      newStaticMethods = newStaticMethods.append(method);
    } else {
      newMethods = newMethods.append(method);
    }
    return newClassBuilder(
        this.fields,
        this.staticFields,
        newMethods,
        newStaticMethods,
        this.constructors);
  }

}
