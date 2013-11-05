package brennus.asm;

import static brennus.model.ExistingType.VOID;
import static brennus.model.Protection.PUBLIC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import brennus.ImmutableList;
import brennus.MethodContext;
import brennus.model.CallConstructorExpression;
import brennus.model.CallConstructorStatement;
import brennus.model.Expression;
import brennus.model.Field;
import brennus.model.FutureType;
import brennus.model.MemberFlags;
import brennus.model.Method;
import brennus.model.Parameter;
import brennus.model.Statement;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
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
    if (classNode.name.indexOf('$') != -1) {
      String declaringClass = classNode.name.substring(0, classNode.name.lastIndexOf("$"));
      classNode.outerClass = declaringClass;
    }
    Iterable<Field> fields = futureType.getFields();
    for (Field field : fields) {
      classNode.fields.add(new FieldNode(
          MethodByteCodeContext.getAccess(field.getFlags()), field.getName(), field.getSignature(), null, null));
    }

    ImmutableList<Method> constructors = futureType.getConstructors();
    if (constructors.isEmpty()) {
      // Add default constructor
      constructors = ImmutableList.from(Arrays.asList(
          new Method(
              futureType.getName(),
              new MemberFlags(false, false, PUBLIC),
              VOID,
              "<init>",
              ImmutableList.<Parameter>empty(),
              ImmutableList.from(Arrays.<Statement>asList(new CallConstructorStatement(0,
                  new CallConstructorExpression(ImmutableList.<Expression>empty()))
              )),
              false)));
    }
    for (Method method : constructors) {
      classNode.methods.add(getMethodNode(futureType, method));
    }

    for (Method method : futureType.getMethods()) {
      classNode.methods.add(getMethodNode(futureType, method));
    }

    for (Method method : futureType.getStaticMethods()) {
      classNode.methods.add(getMethodNode(futureType, method));
    }

//    classNode.interfaces.add(...);

//    classNode.accept(new TraceClassVisitor(new PrintWriter(System.out)));
//    classNode.accept(new ASMifierClassVisitor(new PrintWriter(System.out)));
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    ClassVisitor cv = new CheckClassAdapter(cw, true);

//    ClassVisitor cv = cw;
    classNode.accept(cv);
    return cw.toByteArray();
  }

  private MethodNode getMethodNode(FutureType futureType, Method method) {
    MethodContext methodContext = new MethodContext(futureType, method);
    ASMMethodGenerator statementVisitor = new ASMMethodGenerator(methodContext);
    for (Statement statement : method.getStatements()) {
      statementVisitor.visit(statement);
    }
    MethodNode methodNode = statementVisitor.getMethodNode();
    return methodNode;
  }

}
