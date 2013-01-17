package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.MethodBuilder.MethodHandler;
import brennus.model.CallConstructorStatement;
import brennus.model.Expression;
import brennus.model.Parameter;
import brennus.model.Protection;
import brennus.model.Type;

/**
 *
 * Builder for constructor declarations
 *
 * @author Julien Le Dem
 *
 */
public class ConstructorDeclarationBuilder {

  private final String classIdentifier;
  private final Protection protection;
  private final List<Parameter> parameters = new ArrayList<Parameter>();
  private final MethodHandler methodHandler;
  private final Builder builder;

  ConstructorDeclarationBuilder(String classIdentifier, Protection protection, MethodHandler methodHandler, Builder builder) {
        this.classIdentifier = classIdentifier;
        this.protection = protection;
        this.methodHandler = methodHandler;
        this.builder = builder;
  }

  /**
   * declares a parameter
   * @param type the type of the parameter
   * @param name the name of the parameter
   * @return this
   */
  public ConstructorDeclarationBuilder param(Type type, String name) {
    parameters.add(new Parameter(type, name, parameters.size()));
    return this;
  }

  private ConstructorCallBuilder innerContructorCall() {
    final int sourceLineNumber = builder.getSourceLineNumber();
    return new ConstructorCallBuilder(new ConstructorCallExpressionBuilderFactory(),
        new ExpressionHandler<ConstructorBuilder>() {
      public ConstructorBuilder handleExpression(Expression e) {
        CallConstructorStatement callConstructorStatement = new CallConstructorStatement(sourceLineNumber, e);
        return
            new ConstructorBuilder(classIdentifier, protection, parameters, methodHandler, builder)
            .statementHandler()
            .handleStatement(callConstructorStatement);
      }
    });
  }

  public ConstructorParamExpressionBuilder callSuperConstructor() {
    return innerContructorCall().param();
  }

  public ConstructorBuilder callSuperConstructorNoParam() {
    return innerContructorCall().endConstructorCall();
  }
}
