package brennus.model;

public interface TypeVisitor {

  void visit(ExistingType existingType);

  void visit(FutureType futureType);

}
