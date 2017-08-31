package brennus.asm.specializer;

import java.io.IOException;

import org.junit.Test;

public class TestSpecializer {

  @Test
  public void test() throws Exception {
     InnerNodeFixture innerNodeFixture = new InnerNodeFixture(new InnerNodeFixture(new LeafFixture("a"), "+" , new LeafFixture("b")), "*", new LeafFixture("c"));
     System.out.println(innerNodeFixture + "(d):\n" + innerNodeFixture.foo("d"));
     Specializer specializer = new Specializer();
     InterfaceFixture specialized = specializer.specialize(InterfaceFixture.class, innerNodeFixture);
     System.out.println(specialized + "(e):\n" + specialized.foo("e"));
  }
}
