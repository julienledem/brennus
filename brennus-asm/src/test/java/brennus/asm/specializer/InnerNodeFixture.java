package brennus.asm.specializer;

public class InnerNodeFixture implements InterfaceFixture {

  @Specialized
  private final InterfaceFixture left;
  
  @Specialized
  private final InterfaceFixture right;
  
  private final String operator;

  public InnerNodeFixture(InterfaceFixture left, String operator, InterfaceFixture right) {
    super();
    this.left = left;
    this.operator = operator;
    this.right = right;
  }

  @Override
  public Object foo(Object bar) {
    return left.foo(bar) + " " + operator + " " + right.foo(bar);
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" + left + " " + operator + " " + right +"}";
  }
}
