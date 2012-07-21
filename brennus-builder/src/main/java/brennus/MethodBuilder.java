package brennus;

import java.util.ArrayList;
import java.util.List;

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
public class MethodBuilder extends StatementBuilder<MethodBuilder> {

  interface MethodHandler {
    ClassBuilder handleMethod(Method method);
  }

  private final String classIdentifier;
  private final MemberFlags memberFlags;
  private final Type returnType;
  private final String name;
  private final List<Parameter> parameters;
  private final MethodHandler methodHandler;
  private final List<Statement> statements = new ArrayList<Statement>();


  MethodBuilder(String classIdentifier, MemberFlags memberFlags, Type returnType, String name, List<Parameter> parameters, MethodHandler methodHandler) {
    this.classIdentifier = classIdentifier;
    this.memberFlags = memberFlags;
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
    this.methodHandler = methodHandler;
  }

  public ClassBuilder endMethod() {
    return methodHandler.handleMethod(new Method(classIdentifier, memberFlags, returnType, name, parameters, statements));
  }

  protected StatementHandler<MethodBuilder> statementHandler() {
    return new StatementHandler<MethodBuilder>() {
      public MethodBuilder handleStatement(Statement statement) {
        statements.add(statement);
        return MethodBuilder.this;
      }
    };
  }

}
