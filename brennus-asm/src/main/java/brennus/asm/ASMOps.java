package brennus.asm;

import org.objectweb.asm.Opcodes;

import brennus.model.MemberFlags;
import brennus.model.Type;

class ASMOps implements Opcodes {

  static int getAccess(MemberFlags flags) {
    switch (flags.getProtection()) {
    case PRIVATE:
      return ACC_PRIVATE;
    case PUBLIC:
      return ACC_PUBLIC;
    case PROTECTED:
      return ACC_PROTECTED;
    case DEFAULT:
      return 0;
    default:
        throw new RuntimeException("Unexpected "+flags.getProtection());
    }
  }

  static int getReturn(Type type) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I':
        return IRETURN;
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      return ARETURN;
    }
  }

  static int getLoad(Type type) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I':
        return ILOAD;
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      return ALOAD;
    }
  }

}
