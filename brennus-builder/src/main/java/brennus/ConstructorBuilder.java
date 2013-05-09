package brennus;

import static brennus.model.ExistingType.VOID;
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
public final class ConstructorBuilder extends StatementBuilder<ConstructorBuilder> {

  private final String classIdentifier;
  private final Protection protection;
  private final ImmutableList<Parameter> parameters;
  private final MethodHandler methodHandler;
  private final ImmutableList<Statement> statements;


  ConstructorBuilder(String classIdentifier, Protection protection, ImmutableList<Parameter> parameters, MethodHandler methodHandler, Builder builder) {
    this(
        builder,
        classIdentifier,
        protection,
        parameters,
        methodHandler,
        ImmutableList.<Statement>empty());
  }

  private ConstructorBuilder(
      Builder builder,
      String classIdentifier,
      Protection protection,
      ImmutableList<Parameter> parameters,
      MethodHandler methodHandler,
      ImmutableList<Statement> statements) {
    super(builder);
    this.classIdentifier = classIdentifier;
    this.protection = protection;
    this.parameters = parameters;
    this.methodHandler = methodHandler;
    this.statements = statements;
  }

  public ClassBuilder endConstructor() {
    return methodHandler.handleMethod(new Method(classIdentifier, new MemberFlags(false, false, protection), VOID, "<init>", parameters, statements, false));
  }

  protected StatementHandler<ConstructorBuilder> statementHandler() {
    return new StatementHandler<ConstructorBuilder>() {
      public ConstructorBuilder handleStatement(Statement statement) {
        return new ConstructorBuilder(
            builder,
            classIdentifier,
            protection,
            parameters,
            methodHandler,
            statements.append(statement));
      }
    };
  }

}
