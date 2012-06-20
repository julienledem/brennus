package brennus;

public interface TypeVisitor {

  void visit(ExistingType existingType);

  void visit(FutureType futureType);

}
