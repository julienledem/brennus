package brennus.asm;

import brennus.model.FutureType;

public class ASMTypeGenerator {

  public byte[] generate(FutureType futureType) {
    return new ASMTypeGeneratorVisitor().generate(futureType);
  }

}
