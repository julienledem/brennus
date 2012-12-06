package brennus.asm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.ASMifierMethodVisitor;
import org.objectweb.asm.util.TraceMethodVisitor;

class MethodByteCodeContext implements Opcodes {
  private static final Logger logger = Logger.getLogger(MethodByteCodeContext.class.getName());

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
  private int indent = 0;
  private ASMifierMethodVisitor methodVisitor = new ASMifierMethodVisitor();
  private int currentOp;
  private Map<String, LabelNode> namedLabels = new HashMap<String, LabelNode>();

  MethodByteCodeContext(MethodContext methodContext) {
    Method method = methodContext.getMethod();
    logger.fine(method.toString());
    this.methodNode = new MethodNode(getAccess(method.getFlags()), method.getName(), method.getSignature(), null, null);
    this.methodContext = methodContext;
  }

  public void addInstruction(AbstractInsnNode insnNode, Object... comments) {
    methodNode.instructions.add(insnNode);
    if (logger.isLoggable(Level.FINE)) {
      String s = "";

      insnNode.accept(methodVisitor);
      List<String> text = methodVisitor.getText();
      for (; currentOp < text.size(); currentOp++) {
        String t = text.get(currentOp);
        while (t.length()>0) {
          String current;
          int i = t.indexOf('\n');
          if (i >= 0) {
            current = t.substring(0, i);
            t = t.substring(i+1);
          } else {
            current = t;
            t = "";
          }
          s += indent() + current;
          if (comments!=null && comments.length>0) {
            s += " //";
            for (Object c : comments) {
              s += " " + c;
            }
          }
          if (t.length()>0) {
            s += "\n";
          }
        }
      }
      logger.fine(s);
    }
  }

  public void incIndent(Object... comments) {
    indent ++;
    if (logger.isLoggable(Level.FINE) && comments.length > 0) {
      String log = indent() + "//";
      for (Object object : comments) {
        log += " " + object;
      }
      logger.fine(log);
    }
  }

  public void decIndent() {
    indent--;
  }

  private String indent() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i< indent; ++i) {
      sb.append(" ");
    }
    return sb.toString();
  }

  public void loadThis(Object... comments) {
    load(ALOAD, 0, comments);
  }

  public void load(Type type, int i, Object... comments) {
    load(getLoad(type), i, comments);
  }

  private int getLoad(Type type) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I':
      case 'Z':
        return ILOAD;
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      return ALOAD;
    }
  }

  private void load(int load, int i, Object... comments) {
    maxv = Math.max(maxv, i+1);
    // TODO: better than this
    stack++;
    addInstruction(new VarInsnNode(load, i), comments);
  }

  public MethodNode getMethodNode() {
    if (methodContext.getReturnType().equals(ExistingType.VOID)) {
      addInstruction(new InsnNode(RETURN), "end of method");
    }
    methodNode.visitMaxs(
        Math.max(1,
        methodContext.getMethod().getParameters().size())
        + stack, maxv);
    return methodNode;
  }

  public void unbox(PrimitiveType primitiveType, Object... comment) {
    String boxedClassIdentifier = primitiveType.getBoxedType().getClassIdentifier();
    addInstruction(new TypeInsnNode(CHECKCAST, boxedClassIdentifier), addComment(comment, "unboxing", primitiveType));
    addInstruction(new MethodInsnNode(
        INVOKEVIRTUAL,
        boxedClassIdentifier,
        primitiveType.getName()+"Value",
        "()"+primitiveType.getClassIdentifier()), addComment(comment, "unboxing", primitiveType));
  }

  private Object[] addComment(Object[] comment, Object... moreComment) {
    Object[] result = Arrays.copyOf(comment, comment.length + moreComment.length);
    for (int i = 0; i < moreComment.length; i++) {
      Object object = moreComment[i];
      result[comment.length + i] = object;
    }
    return result;
  }

  public void box(PrimitiveType primitiveType, Object... comment) {
    addInstruction(
        new MethodInsnNode(
            INVOKESTATIC,
            primitiveType.getBoxedType().getClassIdentifier(),
            "valueOf",
            "("+primitiveType.getSignature()+")"+primitiveType.getBoxedType().getSignature()),
            addComment(comment, "unboxing", primitiveType));
  }

  public void cast(Type type, Object... comments) {
    addInstruction(new TypeInsnNode(CHECKCAST, type.getClassIdentifier()), comments);
  }

  public void handleConversion(Type expressionType, Type returnType, final Object... comment) {
    TypeConversion typeConversion = methodContext.getTypeConversion(expressionType, returnType);
    typeConversion.accept(new TypeConversionVisitor() {
      public void visit(UnboxingTypeConversion unboxingTypeConversion) {
        unbox(unboxingTypeConversion.getPrimitiveType(), comment);
      }
      public void visit(BoxingTypeConversion boxingTypeConversion) {
        box(boxingTypeConversion.getPrimitiveType(), comment);
      }
      public void visit(CastTypeConversion castTypeConversion) {
        cast(castTypeConversion.getType(), comment);
      }
    });
  }

  public void push(int bipush, int intValue, Object... comments) {
    // TODO: better than this
    stack++;
    addInstruction(new IntInsnNode(BIPUSH, intValue), comments);
  }

  public void ldc(String value, Object... comments) {
    // TODO: better than this
    stack++;
    addInstruction(new LdcInsnNode(value), comments);
  }

  public void addReturn(Type returnType, Object... comments) {
    addInstruction(new InsnNode(getReturn(returnType)), comments);
  }

  private int getReturn(Type type) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I':
      case 'Z':
        return IRETURN;
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      return ARETURN;
    }
  }

  public void addLineNumber(int line, Object... comments) {
    if (line != 0) {
      LabelNode labelNode = new LabelNode(new Label());
      addLabel(line, labelNode, addComment(comments, "line", line));
    }
  }

  public void addLabel(int line, LabelNode label, Object... comments) {
    addInstruction(label, comments);
    if (line != 0) {
      addInstruction(new LineNumberNode(line, label), comments);
    }
  }

  public void addLabel(LabelNode label, Object... comments) {
    addInstruction(label, comments);
  }

  public void addIConst0(Object... comment) {
    // TODO: better than this
    stack++;
    addInstruction(new InsnNode(ICONST_0), comment);
  }

  public void addIConst1(Object... comment) {
    // TODO: better than this
    stack++;
    addInstruction(new InsnNode(ICONST_1), comment);
  }

  public LabelNode getLabel(String name) {
    LabelNode labelNode = namedLabels.get(name);
    if (labelNode == null) {
      labelNode = new LabelNode();
      namedLabels.put(name, labelNode);
    }
    return labelNode;
  }
}
