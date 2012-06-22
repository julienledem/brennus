package brennus.asm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brennus.model.CallMethodExpression;
import brennus.model.CaseStatement;
import brennus.model.ExistingType;
import brennus.model.ExpressionStatement;
import brennus.model.ExpressionVisitor;
import brennus.model.Field;
import brennus.model.FutureType;
import brennus.model.GetExpression;
import brennus.model.LiteralExpression;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.PrimitiveType;
import brennus.model.ReturnStatement;
import brennus.model.Statement;
import brennus.model.StatementVisitor;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;
import brennus.model.Type;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.ASMifierClassVisitor;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

public class ASMTypeGenerator {
  public byte[] generate(FutureType futureType) {
    return new ASMTypeGeneratorVisitor().generate(futureType);
  }
}
class ASMTypeGeneratorVisitor implements Opcodes, StatementVisitor, ExpressionVisitor {

  private FutureType futureType;
  private ClassNode classNode;
  private MethodNode methodNode;
  private Map<String, Field> fieldsByName = new HashMap<String, Field>();
  private Type lastExpressionType;
  private Method currentMethod;
  private LabelNode currentLabel;
  private int maxv = 0;

  public byte[] generate(FutureType futureType) {
    this.futureType = futureType;
    classNode = new ClassNode();
    classNode.version = Opcodes.V1_6;
    classNode.access = ACC_PUBLIC | ACC_SUPER;
//    classNode.signature ???
    classNode.name = futureType.getClassIdentifier();
    classNode.superName = futureType.getExtending().getClassIdentifier();
    addDefaultConstructor();
    List<Field> fields = futureType.getFields();
    for (Field field : fields) {
      fieldsByName.put(field.getName(), field);
      classNode.fields.add(new FieldNode(
          getAccess(field.getFlags()), field.getName(), field.getSignature(), null, null));
    }

    List<Method> methods = futureType.getMethods();
    for (Method method : methods) {
      maxv = 0;
      currentMethod = method;
      methodNode = new MethodNode(getAccess(method.getFlags()), method.getName(), method.getSignature(), null, null);
      List<Statement> statements = method.getStatements();
      for (Statement statement : statements) {
        statement.accept(this);
      }
//      System.out.println(maxv);
      methodNode.visitMaxs(1, maxv);
      classNode.methods.add(methodNode);
      methodNode = null;
      currentMethod = null;
    }

//    classNode.interfaces.add(...);

//    classNode.accept(new TraceClassVisitor(new PrintWriter(System.out)));
//    classNode.accept(new ASMifierClassVisitor(new PrintWriter(System.out)));
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
//    ClassVisitor cv = new CheckClassAdapter(cw);
    ClassVisitor cv = cw;
    classNode.accept(cv);
    this.futureType = null;
    return cw.toByteArray();
  }

  private int getAccess(MemberFlags flags) {
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

  private void addDefaultConstructor() {
    MethodNode method = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
    load(method, ALOAD, 0);
    method.instructions.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V"));
    method.instructions.add(new InsnNode(RETURN));
    method.visitMaxs(1, 1);
    classNode.methods.add(method);
  }

  private void load(MethodNode method, int load, int i) {
    maxv = Math.max(maxv, i+1);
    method.instructions.add(new VarInsnNode(load, i));
  }

  // Expressions

  @Override
  public void visit(ReturnStatement returnStatement) {
    returnStatement.getExpression().accept(this);
    if (!currentMethod.getReturnType().equals(lastExpressionType)) {
      if (lastExpressionType.isPrimitive() && !currentMethod.getReturnType().isPrimitive()) {
//        System.out.println(lastExpressionType+" =(box)=> "+currentMethod.getReturnType());
        box((PrimitiveType)lastExpressionType);
      } else if (!lastExpressionType.isPrimitive() && currentMethod.getReturnType().isPrimitive()) {
        unbox((PrimitiveType)currentMethod.getReturnType());
      } else if (!lastExpressionType.isPrimitive() && !currentMethod.getReturnType().isPrimitive()) {
        // TODO: check hierarchy
//        if (!lastExpressionType.isAssignableFrom(currentMethod.getReturnType())) {
//          throw new UnsupportedOperationException("TODO "+lastExpressionType+" =(box)=> "+currentMethod.getReturnType());
//        }
      } else {
        // TODO: add cast in between primitives
        throw new UnsupportedOperationException("TODO");
      }
    }
    methodNode.instructions.add(new InsnNode(getReturn(currentMethod.getReturnType())));
    lastExpressionType = null;
  }

  private void unbox(PrimitiveType primitiveType) {
    throw new UnsupportedOperationException("TODO");
  }

  private void box(PrimitiveType primitiveType) {
    methodNode.instructions.add(
        new MethodInsnNode(
            INVOKESTATIC, primitiveType.getBoxedType().getClassIdentifier(), "valueOf", "("+primitiveType.getSignature()+")"+primitiveType.getBoxedType().getSignature()));
  }

  private int getReturn(Type type) {
    if (type.isPrimitive()) {
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

  @Override
  public void visit(ExpressionStatement methodCallStatement) {
    lastExpressionType = null;
    // TODO Auto-generated method stub
  }

  @Override
  public void visit(SwitchStatement switchStatement) {
    switchStatement.getExpression().accept(this);
    List<CaseStatement> caseStatements = switchStatement.getCaseStatements();
    int[] values = new int[caseStatements.size()];
    LabelNode[] labels = new LabelNode[caseStatements.size()];
    for (int i = 0; i < labels.length; i++) {
      labels[i] = new LabelNode();
      values[i] = ((LiteralExpression)caseStatements.get(i).getExpression()).getValue();
    }
    LabelNode defaultLabel = new LabelNode();
    methodNode.instructions.add(new LookupSwitchInsnNode(defaultLabel, values,labels));
    int i=0;
    for (CaseStatement caseStatement : caseStatements) {
      currentLabel = labels[i++];
      caseStatement.accept(this);
      currentLabel = null;
    }
    if (switchStatement.getDefaultCaseStatement() != null) {
      currentLabel = defaultLabel;
      switchStatement.getDefaultCaseStatement().accept(this);
      currentLabel = null;
    } else {
      methodNode.instructions.add(defaultLabel);
    }
    lastExpressionType = null;
  }

  @Override
  public void visit(CaseStatement caseStatement) {
//    System.out.println(caseStatement);
    methodNode.instructions.add(currentLabel);
    methodNode.instructions.add(new FrameNode(F_SAME, 0, null, 0, null));
    List<Statement> statements = caseStatement.getStatements();
    for (Statement statement : statements) {
      statement.accept(this);
    }
    lastExpressionType = null;
  }

  @Override
  public void visit(ThrowStatement throwStatement) {
    throwStatement.getExpression().accept(this);
    methodNode.instructions.add(new InsnNode(ATHROW));
    lastExpressionType = null;
  }

  // Expressions

  @Override
  public void visit(GetExpression getExpression) {
    try {
      Parameter param = getParam(getExpression.getFieldName());
      if (param != null) {
        // TODO: type support
//        System.out.println(getExpression.getFieldName()+" "+param.getIndex());
        load(methodNode, ILOAD, param.getIndex() + 1);
        lastExpressionType = param.getType();
      } else {
        Field field = getField(getExpression.getFieldName());
        if (field!=null) {
          load(methodNode, ALOAD, 0);
          methodNode.instructions.add(new FieldInsnNode(GETFIELD, classNode.name, getExpression.getFieldName(), field.getSignature()));
          lastExpressionType = field.getType();
        } else {
          throw new RuntimeException("can't resolve "+getExpression.getFieldName()+" in current context "+currentMethod);
        }
      }
    } catch (RuntimeException e) {
      throw new RuntimeException("error with "+getExpression, e);
    }
  }

  private Parameter getParam(String fieldName) {
    List<Parameter> parameters = currentMethod.getParameters();
    for (Parameter parameter : parameters) {
      if (parameter.getName().equals(fieldName)) {
        return parameter;
      }
    }
    return null;
  }

  private Field getField(String fieldName) {
    return fieldsByName.get(fieldName);
  }

  @Override
  public void visit(CallMethodExpression callMethodExpression) {
//    System.out.println(callMethodExpression);
    load(methodNode, ALOAD, 0);
    String methodName = callMethodExpression.getMethodName();
    Method method = getMethod(methodName);
    if (method != null) {
//      System.out.println(method);
      methodNode.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, classNode.name, methodName, method.getSignature()));
      lastExpressionType = method.getReturnType();
    } else {
      throw new RuntimeException("can't find method "+methodName+" in hierarchy of "+futureType.getName());
    }
  }

  private Method getMethod(String methodName) {
    return futureType.getMethod(methodName);
  }

  @Override
  public void visit(LiteralExpression literalExpression) {
    lastExpressionType = ExistingType.INT;
    // TODO Auto-generated method stub

  }

}
