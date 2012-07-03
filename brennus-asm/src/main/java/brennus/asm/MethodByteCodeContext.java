package brennus.asm;

import brennus.MethodContext;
import brennus.model.BoxingTypeConversion;
import brennus.model.CastTypeConversion;
import brennus.model.ExistingType;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.PrimitiveType;
import brennus.model.Type;
import brennus.model.TypeConversion;
import brennus.model.TypeConversionVisitor;
import brennus.model.UnboxingTypeConversion;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

class MethodByteCodeContext implements Opcodes {

  public static int getAccess(MemberFlags flags) {
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

  private final MethodContext methodContext;
  private final MethodNode methodNode;
  private int maxv = 0;
  private int stack = 0;

  MethodByteCodeContext(MethodContext methodContext) {
    Method method = methodContext.getMethod();
    this.methodNode = new MethodNode(getAccess(method.getFlags()), method.getName(), method.getSignature(), null, null);
    this.methodContext = methodContext;
  }

  public void addInstruction(AbstractInsnNode insnNode) {
    methodNode.instructions.add(insnNode);
  }

  public void loadThis() {
    load(ALOAD, 0);
  }

  public void load(Type type, int i) {
    load(getLoad(type), i);
  }

  private int getLoad(Type type) {
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

  private void load(int load, int i) {
    maxv = Math.max(maxv, i+1);
    addInstruction(new VarInsnNode(load, i));
  }

  public MethodNode getMethodNode() {
    if (methodContext.getReturnType().equals(ExistingType.VOID)) {
      addInstruction(new InsnNode(RETURN));
    }
    methodNode.visitMaxs(
        Math.max(1,
        methodContext.getMethod().getParameters().size())
        + stack, maxv);
    return methodNode;
  }

  public void unbox(PrimitiveType primitiveType) {
    String boxedClassIdentifier = primitiveType.getBoxedType().getClassIdentifier();
    addInstruction(new TypeInsnNode(CHECKCAST, boxedClassIdentifier));
    addInstruction(new MethodInsnNode(
        INVOKEVIRTUAL,
        boxedClassIdentifier,
        primitiveType.getName()+"Value",
        "()"+primitiveType.getClassIdentifier()));
  }

  public void box(PrimitiveType primitiveType) {
    addInstruction(
        new MethodInsnNode(
            INVOKESTATIC,
            primitiveType.getBoxedType().getClassIdentifier(),
            "valueOf",
            "("+primitiveType.getSignature()+")"+primitiveType.getBoxedType().getSignature()));
  }

  public void cast(Type type) {
    addInstruction(new TypeInsnNode(CHECKCAST, type.getClassIdentifier()));
  }

  public void handleConversion(Type expressionType, Type returnType) {
    TypeConversion typeConversion = methodContext.getTypeConversion(expressionType, returnType);
    typeConversion.accept(new TypeConversionVisitor() {
      public void visit(UnboxingTypeConversion unboxingTypeConversion) {
        unbox(unboxingTypeConversion.getPrimitiveType());
      }
      public void visit(BoxingTypeConversion boxingTypeConversion) {
        box(boxingTypeConversion.getPrimitiveType());
      }
      public void visit(CastTypeConversion castTypeConversion) {
        cast(castTypeConversion.getType());
      }
    });
  }

  public void push(int bipush, int intValue) {
    // TODO: better than this
    stack++;
    addInstruction(new IntInsnNode(BIPUSH, intValue));
  }

  public void ldc(String value) {
    // TODO: better than this
    stack++;
    addInstruction(new LdcInsnNode(value));
  }

  public void addReturn(Type returnType) {
    addInstruction(new InsnNode(getReturn(returnType)));
  }

  private int getReturn(Type type) {
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

  public void addLineNumber(int line) {
    Label l = new Label();
    LabelNode labelNode = new LabelNode(l);
    addLabel(line, labelNode);
  }

  public void addLabel(int line, LabelNode label) {
    methodNode.instructions.add(label);
    methodNode.instructions.add(new LineNumberNode(line, label));
  }
}
