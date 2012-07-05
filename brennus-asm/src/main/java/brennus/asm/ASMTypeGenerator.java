package brennus.asm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import brennus.MethodContext;
import brennus.model.ExistingType;
import brennus.model.Field;
import brennus.model.FutureType;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.Protection;
import brennus.model.Statement;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.util.ASMifierClassVisitor;
import org.objectweb.asm.util.CheckClassAdapter;

public class ASMTypeGenerator {

  public byte[] generate(FutureType futureType) {
    ClassNode classNode = new ClassNode();
    classNode.version = Opcodes.V1_6;
    classNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER;
//    classNode.signature ???
    classNode.sourceFile = futureType.getSourceFile();
    classNode.name = futureType.getClassIdentifier();
    classNode.superName = futureType.getExtending().getClassIdentifier();
    List<Field> fields = futureType.getFields();
    for (Field field : fields) {
      classNode.fields.add(new FieldNode(
          MethodByteCodeContext.getAccess(field.getFlags()), field.getName(), field.getSignature(), null, null));
    }

//    add default constructor
    ASMMethodGenerator defaultConstructor = new ASMMethodGenerator(
        new MethodContext(
            futureType,
            new Method(
                futureType.getClassIdentifier(),
                MemberFlags.fromKeywords(Protection.PUBLIC),
                ExistingType.VOID, "<init>",
                new ArrayList<Parameter>(),
                new ArrayList<Statement>())));
    defaultConstructor.addDefaultConstructorStatements();
    classNode.methods.add(defaultConstructor.getMethodNode());

    List<Method> methods = futureType.getMethods();
    for (Method method : methods) {
      MethodContext methodContext = new MethodContext(futureType, method);
      ASMMethodGenerator statementVisitor = new ASMMethodGenerator(methodContext);
      List<Statement> statements = method.getStatements();
      for (Statement statement : statements) {
        statementVisitor.visit(statement);
      }
//      System.out.println(maxv);

      classNode.methods.add(statementVisitor.getMethodNode());
      methodContext = null;
    }

//    classNode.interfaces.add(...);

//    classNode.accept(new TraceClassVisitor(new PrintWriter(System.out)));
    classNode.accept(new ASMifierClassVisitor(new PrintWriter(System.out)));
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    ClassVisitor cv = new CheckClassAdapter(cw, true);
//    ClassVisitor cv = cw;
    classNode.accept(cv);
    return cw.toByteArray();
  }

}
