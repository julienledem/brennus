package brennus.asm;

import brennus.MethodContext;
import brennus.model.BoxingTypeConversion;
import brennus.model.CastTypeConversion;
import brennus.model.Method;
import brennus.model.PrimitiveType;
import brennus.model.Type;
import brennus.model.TypeConversion;
import brennus.model.TypeConversionVisitor;
import brennus.model.UnboxingTypeConversion;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

class MethodByteCodeContext implements Opcodes {

  private final MethodContext methodContext;
  private final MethodNode methodNode;
  private int maxv = 0;

  MethodByteCodeContext(MethodContext methodContext) {
    Method method = methodContext.getMethod();
    this.methodNode = new MethodNode(ASMOps.getAccess(method.getFlags()), method.getName(), method.getSignature(), null, null);
    this.methodContext = methodContext;
  }

  public void addInstruction(AbstractInsnNode insnNode) {
    getMethodNode().instructions.add(insnNode);
  }

  public void load(int load, int i) {
    maxv = Math.max(maxv, i+1);
    addInstruction(new VarInsnNode(load, i));
  }

  public MethodNode getMethodNode() {
    methodNode.visitMaxs(
        Math.max(1,
        methodContext.getMethod().getParameters().size()), maxv);
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
}
