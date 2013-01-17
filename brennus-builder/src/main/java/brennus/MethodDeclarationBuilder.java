package brennus;

import java.util.ArrayList;
import java.util.List;

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
  private final List<Parameter> parameters = new ArrayList<Parameter>();

  MethodDeclarationBuilder(String classIdentifier, MemberFlags memberFlags,
      Type returnType, String methodName, MethodHandler methodHandler, Builder builder) {
    super(builder);
    this.classIdentifier = classIdentifier;
    this.memberFlags = memberFlags;
    this.returnType = returnType;
    this.methodName = methodName;
    this.methodHandler = methodHandler;
  }

  /**
   * declares a parameter
   * @param type the type of the parameter
   * @param name the name of the parameter
   * @return this
   */
  public MethodDeclarationBuilder param(Type type, String name) {
    parameters.add(new Parameter(type, name, parameters.size()));
    return this;
  }

  protected StatementHandler<MethodBuilder> statementHandler() {
    return new MethodBuilder(classIdentifier, memberFlags, returnType, methodName, parameters, methodHandler, builder).statementHandler();
  }

}
