package brennus;

import static brennus.model.ExistingType.VOID;

import java.util.ArrayList;
import java.util.List;

import brennus.MethodBuilder.MethodHandler;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.Protection;
import brennus.model.Statement;

/**
 *
 * Builds a constructor
 *
 * @author Julien Le Dem
 *
 */
public class ConstructorBuilder extends StatementBuilder<ConstructorBuilder> {

  private final String classIdentifier;
  private final Protection protection;
  private final List<Parameter> parameters;
  private final MethodHandler methodHandler;
  private final List<Statement> statements = new ArrayList<Statement>();


  ConstructorBuilder(String classIdentifier, Protection protection, List<Parameter> parameters, MethodHandler methodHandler, Builder builder) {
    super(builder);
    this.classIdentifier = classIdentifier;
    this.protection = protection;
    this.parameters = parameters;
    this.methodHandler = methodHandler;
  }

  public ClassBuilder endConstructor() {
    return methodHandler.handleMethod(new Method(classIdentifier, new MemberFlags(false, false, protection), VOID, "<init>", parameters, statements, false));
  }

  protected StatementHandler<ConstructorBuilder> statementHandler() {
    return new StatementHandler<ConstructorBuilder>() {
      public ConstructorBuilder handleStatement(Statement statement) {
        statements.add(statement);
        return ConstructorBuilder.this;
      }
    };
  }

}
