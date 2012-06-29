package brennus;

import java.util.List;

import brennus.model.BoxingTypeConversion;
import brennus.model.CastTypeConversion;
import brennus.model.Field;
import brennus.model.FieldAccessType;
import brennus.model.FutureType;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.ParameterAccessType;
import brennus.model.PrimitiveType;
import brennus.model.Type;
import brennus.model.TypeConversion;
import brennus.model.UnboxingTypeConversion;
import brennus.model.VarAccessType;

public class MethodContext {

  private final FutureType type;
  private final Method method;

  public MethodContext(FutureType type, Method method) {
    this.type = type;
    this.method = method;
  }

  private Parameter getParam(String fieldName) {
    List<Parameter> parameters = method.getParameters();
    for (Parameter parameter : parameters) {
      if (parameter.getName().equals(fieldName)) {
        return parameter;
      }
    }
    return null;
  }

  private Field getField(String varName) {
    Field field = getField(type.getFields(), varName);
    if (field == null) {
      field = getField(type.getStaticFields(), varName);
    }
    return field;
  }

  private Field getField(List<Field> fields, String varName) {
    for (Field field : fields) {
      if (field.getName().equals(varName)) {
        return field;
      }
    }
    return null;
  }

  public VarAccessType getVarAccessType(String varName) {
    // TODO local vars
    Parameter param = getParam(varName);
    if (param != null) {
      return new ParameterAccessType(param);
    } else {
      Field field = getField(varName);
      if (field!=null) {
        return new FieldAccessType(field);
      } else {
        throw new RuntimeException("can't resolve "+varName+" in current context "+this);
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
      throw new UnsupportedOperationException("TODO");
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
}
