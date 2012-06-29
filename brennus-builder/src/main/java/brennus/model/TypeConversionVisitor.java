package brennus.model;

public interface TypeConversionVisitor {

  void visit(BoxingTypeConversion boxingTypeConversion);

  void visit(UnboxingTypeConversion unboxingTypeConversion);

  void visit(CastTypeConversion castTypeConversion);

}
