package brennus.asm;

import brennus.model.FutureType;

public class DynamicClassLoader extends ClassLoader {
  ASMTypeGenerator asmTypeGenerator = new ASMTypeGenerator();

  public Class<?> define(FutureType type) {
    byte[] classBytes = asmTypeGenerator.generate(type);
    return super.defineClass(type.getName(), classBytes, 0, classBytes.length);
  }
}