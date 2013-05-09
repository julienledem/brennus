package brennus;

import brennus.MethodBuilder.MethodHandler;
import brennus.model.MemberFlags;
import brennus.model.Parameter;
import brennus.model.Type;

/**
 *
 * Builder for method declarations
 *
 * @author Julien Le Dem
 *
 */
public class MethodDeclarationBuilder extends StatementBuilder<MethodBuilder> {

  private final String classIdentifier;
  private final MemberFlags memberFlags;
  private final Type returnType;
  private final String methodName;
  private final MethodHandler methodHandler;
  private final ImmutableList<Parameter> parameters;

  MethodDeclarationBuilder(
      String classIdentifier,
      MemberFlags memberFlags,
      Type returnType,
      String methodName,
      MethodHandler methodHandler,
      Builder builder) {
    this(
        builder,
        classIdentifier,
        memberFlags,
        returnType,
        methodName,
        methodHandler,
        ImmutableList.<Parameter>empty());
  }


  private MethodDeclarationBuilder(
      Builder builder,
      String classIdentifier,
      MemberFlags memberFlags,
      Type returnType,
      String methodName,
      MethodHandler methodHandler,
      ImmutableList<Parameter> parameters) {
    super(builder);
    this.classIdentifier = classIdentifier;
    this.memberFlags = memberFlags;
    this.returnType = returnType;
    this.methodName = methodName;
    this.methodHandler = methodHandler;
    this.parameters = parameters;
  }


  /**
   * declares a parameter
   * @param type the type of the parameter
   * @param name the name of the parameter
   * @return this
   */
  public MethodDeclarationBuilder param(Type type, String name) {
    return new MethodDeclarationBuilder(
            builder,
            classIdentifier,
            memberFlags,
            returnType,
            methodName,
            methodHandler,
            parameters.append(new Parameter(type, name, parameters.size())));
  }

  protected StatementHandler<MethodBuilder> statementHandler() {
    return new MethodBuilder(classIdentifier, memberFlags, returnType, methodName, parameters, methodHandler, builder).statementHandler();
  }

}
