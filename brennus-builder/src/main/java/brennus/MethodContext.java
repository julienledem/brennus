package brennus;

import java.util.HashMap;
import java.util.Map;

import brennus.model.BoxingTypeConversion;
import brennus.model.CastTypeConversion;
import brennus.model.Field;
import brennus.model.FieldAccessType;
import brennus.model.FutureType;
import brennus.model.LocalVariableAccessType;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.ParameterAccessType;
import brennus.model.PrimitiveType;
import brennus.model.Type;
import brennus.model.TypeConversion;
import brennus.model.UnboxingTypeConversion;
import brennus.model.VarAccessType;

public final class MethodContext {

  private final FutureType type;
  private final Method method;
  private final Map<String, LocalVarContext> localVars = new HashMap<String, LocalVarContext>();

  public MethodContext(FutureType type, Method method) {
    this.type = type;
    this.method = method;
  }

  private Parameter getParam(String fieldName) {
    Iterable<Parameter> parameters = method.getParameters();
    for (Parameter parameter : parameters) {
      if (parameter.getName().equals(fieldName)) {
        return parameter;
      }
    }
    return null;
  }

  public VarAccessType getVarAccessType(String varName) {

    Parameter param = getParam(varName);
    if (param != null) {
      return new ParameterAccessType(param);
    } else {
      Field field = type.getField(varName);
      if (field!=null) {
        return new FieldAccessType(field);
      } else {
        LocalVarContext localVarContext;
        if (!localVars.containsKey(varName)) {
          throw new RuntimeException("can not access " + varName + " in parameters, local variables or fields of " + type.getName() + "." + method.getName() + method.getSignature());
        } else {
          localVarContext = localVars.get(varName);
        }
        return new LocalVariableAccessType(varName, localVarContext.getIndex(), localVarContext.getType());
      }
    }

  }

  @Override
  public String toString() {
    return "["+this.getClass().getSimpleName()+" "+method+"]";
  }

  public Type getReturnType() {
    return method.getReturnType();
  }

  public TypeConversion getTypeConversion(Type from, Type to) {
    if (to.equals(from)) {
      return TypeConversion.NONE;
    }
    if (from.isPrimitive() && !to.isPrimitive()) {
      return new BoxingTypeConversion((PrimitiveType) from);
    } else if (!from.isPrimitive() && to.isPrimitive()) {
      return new UnboxingTypeConversion((PrimitiveType) to);
    } else if (!from.isPrimitive() && !to.isPrimitive()) {
      if (to.isAssignableFrom(from)) {
        return TypeConversion.NONE;
      } else {
        return new CastTypeConversion(to);
      }
    } else {
      // TODO: add cast in between primitives
      throw new UnsupportedOperationException("TODO: convert " + from + " to " + to);
    }

  }

  public Method getMethod() {
    return this.method;
  }

  public String getClassIdentifier() {
    return type.getClassIdentifier();
  }

  public FutureType getType() {
    return type;
  }

  public LocalVarContext defineLocalVar(Type type, String varName) {
    // TODO: deal with scope
    if (localVars.containsKey(varName)) {
      throw new RuntimeException("Duplicate local variable "+varName+ " in method "+method.toString());
    }
    LocalVarContext context = new LocalVarContext(varName, localVars.size(), type);
    localVars.put(varName, context);
    return context;
  }
}
