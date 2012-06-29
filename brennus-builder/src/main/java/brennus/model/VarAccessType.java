package brennus.model;

abstract public class VarAccessType {
  abstract public void accept(VarAccessTypeVisitor visitor);
}
