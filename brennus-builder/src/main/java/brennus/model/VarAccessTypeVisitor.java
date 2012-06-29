package brennus.model;

public interface VarAccessTypeVisitor {

  void visit(FieldAccessType fieldAccessType);

  void visit(ParameterAccessType parameterAccessType);

}
