package brennus;

public class ExistingType extends Type {

  private final Class<?> existing;

  ExistingType(Class<?> existing) {
    this.existing = existing;
  }

  @Override
  public void visit(TypeVisitor typeVisitor) {
    typeVisitor.visit(this);
  }

  public Class<?> getExisting() {
    return existing;
  }

  @Override
  public String getName() {
    return existing.getName();
  }

}
