package brennus.model;

import static brennus.model.ExceptionHandlingVisitor.wrap;

public final class FieldAccessType extends VarAccessType {

  private final Field field;

  public FieldAccessType(Field field) {
    this.field = field;
  }

  public Field getField() {
    return field;
  }

  @Override
  public void accept(VarAccessTypeVisitor visitor) {
    wrap(visitor).visit(this);
  }

}
