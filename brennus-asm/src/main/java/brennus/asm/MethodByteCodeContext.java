package brennus.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import brennus.MethodContext;
import brennus.model.BoxingTypeConversion;
import brennus.model.CastTypeConversion;
import brennus.model.ExistingType;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
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
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.ASMifierMethodVisitor;

class MethodByteCodeContext implements Opcodes {
  private static final Logger logger = Logger.getLogger(MethodByteCodeContext.class.getName());

  public static int getAccess(MemberFlags flags) {
    int acc = (flags.isStatic() ? ACC_STATIC : 0) | (flags.isFinal() ? ACC_FINAL : 0);
    switch (flags.getProtection()) {
    case PRIVATE:
      return acc | ACC_PRIVATE;
    case PUBLIC:
      return acc | ACC_PUBLIC;
    case PROTECTED:
      return acc | ACC_PROTECTED;
    case DEFAULT:
      return acc;
    default:
        throw new RuntimeException("Unexpected " + flags.getProtection());
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
  private Set<String> definedNamedLabels = new HashSet<String>();
  private int currentLocalVariableByteCodeIndex = 0;
  private List<Integer> localVarIndexToBytecodeIndex = new ArrayList<Integer>();
  private List<Integer> paramIndexToBytecodeIndex = new ArrayList<Integer>();

  MethodByteCodeContext(MethodContext methodContext) {
    Method method = methodContext.getMethod();
    logger.fine(method.toString());
    this.methodNode = new MethodNode(getAccess(method.getFlags()), method.getName(), method.getSignature(), null, null);
    this.methodContext = methodContext;
    Iterable<Parameter> parameters = methodContext.getMethod().getParameters();
    if (!methodContext.getMethod().isStatic()) {
      currentLocalVariableByteCodeIndex = 1;
    }
    for (Parameter parameter : parameters) {
      paramIndexToBytecodeIndex.add(currentLocalVariableByteCodeIndex);
      incLocalVarIndex(parameter.getType());
    }

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

  public void aload(Type type, Object... comments) {
    // TODO: better than this
    stack++;
    addInstruction(new InsnNode(getALoad(type)), comments);
  }

  private void load(int load, int i, Object... comments) {
    maxv = Math.max(maxv, i+1);
    // TODO: better than this
    stack++;
    addInstruction(new VarInsnNode(load, i), comments);
  }

  private int getALoad(Type type) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I': // int
      case 'Z': // boolean
        return IALOAD;
      case 'J': // long
        return LALOAD;
      case 'F': // float
        return FALOAD;
      case 'D': // double
        return DALOAD;
      case 'C': // char
      case 'V': // void
      case 'B': // byte
      case 'S': // short
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      return AALOAD;
    }
  }

  private int getLoad(Type type) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I': // int
      case 'Z': // boolean
        return ILOAD;
      case 'J': // long
        return LLOAD;
      case 'F': // float
        return FLOAD;
      case 'D': // double
        return DLOAD;
      case 'C': // char
      case 'V': // void
      case 'B': // byte
      case 'S': // short
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      return ALOAD;
    }
  }

  public void store(Type type, int i, Object... comments) {
    store(getStore(type), i, comments);
  }

  private void store(int store, int i, Object... comments) {
    maxv = Math.max(maxv, i+1);
    addInstruction(new VarInsnNode(store, i), comments);
  }

  private int getStore(Type type) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I': // int
      case 'Z': // boolean
        return ISTORE;
      case 'J': // long
        return LSTORE;
      case 'F': // float
        return FSTORE;
      case 'D': // double
        return DSTORE;
      case 'C': // char
      case 'V': // void
      case 'B': // byte
      case 'S': // short
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      return ASTORE;
    }
  }

  public MethodNode getMethodNode() {
    if (methodContext.getReturnType().equals(ExistingType.VOID)) {
      addInstruction(new InsnNode(RETURN), "end of method");
    }
    // TODO: seriously fix the following
    methodNode.visitMaxs(
        Math.max(1, maxv) + stack,
        Math.max(maxv + 1, currentLocalVariableByteCodeIndex));
    // validate that all gotos jump to label that have been defined
    // otherwise we can get "Execution can fall off end of the code" errors
    for (String label : namedLabels.keySet()) {
      if (!definedNamedLabels.contains(label)) {
        throw new RuntimeException("\"goto "+label+"\" without corresponding \""+label+":\"");
      }
    }
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

  public void ldc(Object value, Object... comments) {
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
      case 'I': // int
      case 'Z': // boolean
        return IRETURN;
      case 'J': // long
        return LRETURN;
      case 'F': // float
        return FRETURN;
      case 'D': // float
        return DRETURN;
      default:
        throw new RuntimeException("Unsupported " + type + " " + type.getClassIdentifier().charAt(0));
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

  public LabelNode getLabelForSwitchGotoCase(String name) {
    return getLabel(name);
  }

  private LabelNode getLabel(String name) {
    LabelNode labelNode = namedLabels.get(name);
    if (labelNode == null) {
      labelNode = new LabelNode();
      namedLabels.put(name, labelNode);
    }
    return labelNode;
  }

  public void addNamedLabel(int line, String name) {
    LabelNode labelNode = getLabel(name);
    addLabel(line, labelNode, name+":");
    definedNamedLabels.add(name);
  }

  public void gotoLabel(String name) {
    LabelNode labelNode = getLabel(name);
    addInstruction(new JumpInsnNode(GOTO, labelNode), "goto "+name);
  }

  public int getLocalVariableByteCodeIndex(int varIndex) {
    // TODO: long and double take 2 slots
    // TODO: static methods don't have this
//    return methodContext.getMethod().getParameters().size() + 1 /* this */ + varIndex;
    return localVarIndexToBytecodeIndex.get(varIndex);
  }

  public void defineLocalVar(Type type, String name, int index) {
    localVarIndexToBytecodeIndex.add(currentLocalVariableByteCodeIndex);
    incLocalVarIndex(type);
  }

  private void incLocalVarIndex(Type type) {
    if (!type.isPrimitive()) {
      ++ currentLocalVariableByteCodeIndex;
    } else {
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I': // int
      case 'Z': // boolean
      case 'F': // float
      case 'C': // char
      case 'V': // void
      case 'B': // byte
      case 'S': // short
        ++ currentLocalVariableByteCodeIndex;
        break;
      case 'J': // long
      case 'D': // double
        currentLocalVariableByteCodeIndex += 2;
        break;
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    }
  }

  public int getParamByteCodeIndex(int paramIndex) {
    return paramIndexToBytecodeIndex.get(paramIndex);
  }

  public void dup(Object... comments) {
    addInstruction(new InsnNode(DUP), comments);
    // TODO: better than this
    stack++;
  }

  abstract public class ByteCodeBuilder<T> {

    abstract T self();

    public T addBool(boolean b, Object... comment) {
      MethodByteCodeContext.this.addBool(b, comment);
      return self();
    }

    public T addIConst0(Object... comment) {
      MethodByteCodeContext.this.addIConst0(comment);
      return self();
    }

    public T addIConst1(Object... comment) {
      MethodByteCodeContext.this.addIConst1(comment);
      return self();
    }
  }

  public class Else extends ByteCodeBuilder<Else> {
    private LabelNode thenCase;
    private LabelNode endIf;

    Else(int jumpInst, Object... comment) {
      // TODO: combine with parent
      thenCase = new LabelNode();
      endIf = new LabelNode();
      addInstruction(new JumpInsnNode(jumpInst, thenCase), comment);
    }

    @Override
    Else self() {
      return this;
    }

    /**
     * @return object to add instructions in then case
     */
    public Then thenCase() {
      addInstruction(new JumpInsnNode(GOTO, endIf), "end else, skip then");
      addLabel(thenCase, "then label");
      return new Then(endIf);
    }

  }

  public class Then extends ByteCodeBuilder<Then>  {
    private final LabelNode endIf;

    Then(LabelNode endIf) {
      this.endIf = endIf;
    }

    @Override
    Then self() {
      return this;
    }

    /**
     * ends the if
     */
    public void endIf() {
      addLabel(endIf, "endIf");
    }
  }

  /**
   * in bytecode if/then/else is laid out if/else/then:
   * if (cond) jump to then
   * {else}
   * jump to endIf
   * then:
   * {then}
   * endIf:
   * @param jumpInst
   * @param comment
   * @return object to add instruction in else case
   */
  public Else ifCondElse(int jumpInst, Object... comment) {
    return new Else(jumpInst, comment);
  }

  public void addBool(boolean b, Object... comment) {
    if (b) {
      addIConst1("bool literal", b);
    } else {
      addIConst0("bool literal", b);
    }
  }

  public void newArray(Type type, Object... comments) {
    if (type.isPrimitive()) {
      // TODO: other primitive types
      switch (type.getClassIdentifier().charAt(0)) {
      case 'I': // int
        addInstruction(new IntInsnNode(NEWARRAY, T_INT), comments);
        break;
      case 'Z': // boolean
        addInstruction(new IntInsnNode(NEWARRAY, T_BOOLEAN), comments);
        break;
      case 'J': // long
        addInstruction(new IntInsnNode(NEWARRAY, T_LONG), comments);
        break;
      case 'F': // float
        addInstruction(new IntInsnNode(NEWARRAY, T_FLOAT), comments);
        break;
      case 'D': // double
        addInstruction(new IntInsnNode(NEWARRAY, T_DOUBLE), comments);
        break;
      case 'C': // char
        addInstruction(new IntInsnNode(NEWARRAY, T_CHAR), comments);
        break;
      case 'B': // byte
        addInstruction(new IntInsnNode(NEWARRAY, T_BYTE), comments);
        break;
      case 'S': // short
        addInstruction(new IntInsnNode(NEWARRAY, T_SHORT), comments);
        break;
      case 'V': // void
      default:
        throw new RuntimeException("Unsupported "+type);
      }
    } else {
      addInstruction(new TypeInsnNode(ANEWARRAY, type.getClassIdentifier()), comments);
    }
  }

}
