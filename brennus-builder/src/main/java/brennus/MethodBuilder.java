package brennus;

import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.Statement;
import brennus.model.Type;

/**
 *
 * Builds a method
 *
 * @author Julien Le Dem
 *
 */
public final class MethodBuilder extends StatementBuilder<MethodBuilder> {

  interface MethodHandler {
    ClassBuilder handleMethod(Method method);
  }

  private final String classIdentifier;
  private final MemberFlags memberFlags;
  private final Type returnType;
  private final String name;
  private final ImmutableList<Parameter> parameters;
  private final MethodHandler methodHandler;
  private final ImmutableList<Statement> statements;

  MethodBuilder(String classIdentifier, MemberFlags memberFlags, Type returnType, String name, ImmutableList<Parameter> parameters, MethodHandler methodHandler, Builder builder) {
    this(
        builder,
        classIdentifier,
        memberFlags,
        returnType,
        name,
        parameters,
        methodHandler,
        ImmutableList.<Statement>empty());
  }

  private MethodBuilder(
      Builder builder,
      String classIdentifier,
      MemberFlags memberFlags,
      Type returnType,
      String name,
      ImmutableList<Parameter> parameters,
      MethodHandler methodHandler,
      ImmutableList<Statement> statements) {
    super(builder);
    this.classIdentifier = classIdentifier;
    this.memberFlags = memberFlags;
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
    this.methodHandler = methodHandler;
    this.statements = statements;
  }

  public ClassBuilder endMethod() {
    return methodHandler.handleMethod(new Method(classIdentifier, memberFlags, returnType, name, parameters, statements, false));
  }

  protected StatementHandler<MethodBuilder> statementHandler() {
    return new StatementHandler<MethodBuilder>() {
      public MethodBuilder handleStatement(Statement statement) {
        return new MethodBuilder(
            builder,
            classIdentifier,
            memberFlags,
            returnType,
            name,
            parameters,
            methodHandler,
            statements.append(statement));
      }
    };
  }

  public <S> S map(Function<MethodBuilder, S> function) {
    return function.apply(this);
  }
}
