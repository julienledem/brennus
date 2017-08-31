package brennus.asm.specializer;

public class LeafFixture implements InterfaceFixture {

  private final String val;

  
  
  public LeafFixture(String val) {
    super();
    this.val = val;
  }



  @Override
  public Object foo(Object bar) {
    return  val + "(" + bar + ")";
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" + val + "}";
  }
}
