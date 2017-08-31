package brennus.asm.specializer;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.CheckClassAdapter;

import brennus.asm.specializer.Specializer.FieldDescriptor;
import brennus.model.TypeIdentifier;

public class Specializer extends ClassLoader {

  static int id;
  
  private static class Logger implements AutoCloseable {

    private static int indent = 0;
    private String block;
    
    Logger(String block) {
      this.block = block;
      log(block + " {");
      ++ indent;
    }
    
    public void log(String message) {
      for (int i = 0; i < indent; i++) {
        System.out.print("  ");
      }
      System.out.println(message);
    }
    
    @Override
    public void close() {
      -- indent;
      log("} // " + block);
    }
    
  }
  
  public <T, U extends T> T specialize(Class<T> parent, U t) throws SpecializationException {
    try (Logger l = new Logger("ROOT specialize(" + parent.getSimpleName() + " " + t + ")")) {
      ClassDescriptor specializedClass = specializeClass(t.getClass(), t);
      return parent.cast(specializeInstance(specializedClass, t));
    }
  }
  
  /**
   * creates a specialized copy of the given instance
   * @param specializedClass target specialized class to create an instance of
   * @param t the object to copy and specialize
   * @return the specialized instance. Should be functionally equivalent to t
   */
  private Object specializeInstance(ClassDescriptor specializedClass, Object t) {
    try (Logger l = new Logger("specializeInstance(" + specializedClass.getSpecializedClass().getSimpleName() + " , " + t + ")")) {
      if (!specializedClass.isSpecialized()) {
        l.log("value unchanged");
        return t;
      }
      l.log("Creating new instance");
      Object newInstance = specializedClass.getSpecializedClass().newInstance();
      l.log("setting field values:");
      Class<?> c = t.getClass();
      l.log("from class " + c.getSimpleName() + " to class " + specializedClass.getSpecializedClass().getSimpleName());
      while (c != null && specializedClass != null) {
        for (Field field : c.getDeclaredFields()) {
          field.setAccessible(true);
          Object value = field.get(t);
          FieldDescriptor fieldDescriptor = specializedClass.getFields().get(field.getName());
          if (fieldDescriptor != null && fieldDescriptor.getSpecializedClass().isSpecialized()) {
            value = specializeInstance(fieldDescriptor.getSpecializedClass(), value);
            l.log("- specialized " + newInstance.getClass().getSimpleName() + "." + field.getName() + " = " + value);
          } else {
            l.log("- copied " + newInstance.getClass().getSimpleName() + "." + field.getName() + " = " + value);
          }
          Field newField = newInstance.getClass().getField(field.getName());
          newField.set(newInstance, value);
        }
        c = c.getSuperclass();
        specializedClass = specializedClass.getParent();
      }
      return newInstance;
    } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
      throw new SpecializationException("Could not create instance of the specialized class " + specializedClass.specializedClass.getSimpleName(), e);
    }
  }
 
  
  private ClassDescriptor specializeClass(Class<?> originalClass, Object t) {
    try (Logger l = new Logger("specializeClass(" + originalClass.getSimpleName() + ", " + t + ")")) {
      ClassNode genericClassNode = new ClassNode();
      ClassReader cr = new ClassReader(originalClass.getName());

      cr.accept(genericClassNode, 0);

      ClassNode specificClassNode = new ClassNode();
      specificClassNode.version = Opcodes.V1_6;
      specificClassNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER;
      specificClassNode.sourceFile = genericClassNode.sourceFile;
      specificClassNode.superName = genericClassNode.superName;
      specificClassNode.interfaces = genericClassNode.interfaces;
      specificClassNode.outerClass = genericClassNode.outerClass;

      List<FieldNode> fields = (List<FieldNode>)genericClassNode.fields;
      String specializedSuffix = "$Specialized" + (id++);
      LinkedHashMap<String, FieldDescriptor> fieldValues = new LinkedHashMap<String, FieldDescriptor>();
      
      ClassDescriptor specializedParentClass = null;
      boolean specialized = false;
      // specialize superclass
      if (!genericClassNode.superName.equals("java/lang/Object")) {
//        throw new RuntimeException(genericClassNode.superName);
        l.log("specializing parent:");
        specializedParentClass = specializeClass(originalClass.getSuperclass(), t);
        if (specializedParentClass == null) {
          specificClassNode.superName = genericClassNode.superName;
          l.log("keeping parent class: " + originalClass.getSuperclass().getSimpleName());
        } else {
          specialized = true;
          specializedSuffix += "_くsuperニ" + specializedParentClass.getSpecializedClass().getSimpleName() + "ᐳ";
          specificClassNode.superName = specializedParentClass.getSpecializedClass().getName().replace('.', '/');
          l.log("specialized parent: " + specializedParentClass.getSpecializedClass().getSimpleName());
        }
      }
      
      Map<String, FieldTranslation> originalDescToFieldTranslation = new HashMap<>();
      Map<String, FieldDescriptor> parentTranslations = mapTranslations(originalDescToFieldTranslation, specializedParentClass);
      
      specializedSuffix += "_";
      // specialize fields
      l.log("Specializing fields:");
      for(FieldNode field: fields) {
        //      l.log("name=" + field.name + " desc=" + field.desc+ " sig=" + field.signature);
        //      field.
        //      l.log("getting field value: " + field.name + " = " + value);
        if (isAnnotated(field)) {
          l.log(" - specialized field: " + field.name);
          Field declaredField = originalClass.getDeclaredField(field.name);
          declaredField.setAccessible(true);
          Object fieldValue = declaredField.get(t);
          ClassDescriptor specializedFieldType = specializeClass(fieldValue.getClass(), fieldValue);
          FieldDescriptor desc = new FieldDescriptor(field.name, specializedFieldType);
          field.desc = getClassDesc(specializedFieldType.getSpecializedClass());
          l.log("  specialized field: " + specializedFieldType.getSpecializedClass().getSimpleName());
          specialized = true;
          fieldValues.put(field.name, desc);
          specializedSuffix += "_く" + field.name + "ニ" + desc.getSpecializedClass().getSpecializedClass().getSimpleName() + "ᐳ";
        } else {
          l.log(" - regular field: " + field.name);
        }
        field.access = ACC_PUBLIC;
        specificClassNode.fields.add(field);
      }

      if (!specialized) {
        l.log("no specialization for " + originalClass.getSimpleName());
        return new ClassDescriptor(specializedParentClass, originalClass, originalClass, new LinkedHashMap<String, FieldDescriptor>(), false);
      }
      
      final String specializedClassName = originalClass.getName() + specializedSuffix;
      specificClassNode.name = genericClassNode.name + specializedSuffix;
      Map<String, FieldDescriptor> fieldMappings = new HashMap<>(parentTranslations);
      fieldMappings.putAll(fieldValues);
      originalDescToFieldTranslation.put(genericClassNode.name, new FieldTranslation(genericClassNode.name, specificClassNode.name, fieldMappings));

      List<MethodNode> methods = (List<MethodNode>)genericClassNode.methods;
      for(MethodNode method: methods) {
        //      l.log("name=" + method.name + " desc=" + method.desc);
        if (!method.name.equals("<init>")) {
          MethodNode specializedMethod = specializeMethod(originalDescToFieldTranslation, method);
          specificClassNode.methods.add(specializedMethod);
        }
      }

      MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
      constructor.instructions.add(new VarInsnNode(ALOAD, 0));
      String parentClass = specializedParentClass == null ? "java/lang/Object" : specializedParentClass.getSpecializedClass().getName().replace('.', '/');
      constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, parentClass, "<init>", "()V"));
      constructor.instructions.add(new InsnNode(RETURN));
      constructor.visitMaxs(2, 2); // TODO: right values
      specificClassNode.methods.add(constructor);

      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
      ClassVisitor cv = new CheckClassAdapter(cw, true);

      //      ClassVisitor cv = cw;
      specificClassNode.accept(cv);

      byte[] classBytes = cw.toByteArray();
      Class<?> specializedClass = super.defineClass(specializedClassName, classBytes, 0, classBytes.length);
      l.log("New specialized class " + originalClass.getName() + " => " + specializedClass.getName());
      return new ClassDescriptor(specializedParentClass, originalClass, specializedClass, fieldValues, true);
    } catch (IOException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new SpecializationException("Could not specialize class definition", e);
    }
  }

  private Map<String, FieldDescriptor> mapTranslations(Map<String, FieldTranslation> originalDescToFieldTranslation, ClassDescriptor currentParent) {
    if (currentParent != null && currentParent.isSpecialized()) {
      Map<String, FieldDescriptor> parentTranslations = mapTranslations(originalDescToFieldTranslation, currentParent.getParent());
      String parentDesc = classDesc(currentParent.originalClass);
      String specializedDesc = classDesc(currentParent.specializedClass);
      Map<String, FieldDescriptor> fields = new HashMap<>(parentTranslations); 
      fields.putAll(currentParent.getFields());
      originalDescToFieldTranslation.put(parentDesc, new FieldTranslation(parentDesc, specializedDesc, fields));
      return fields;
    }
    return new HashMap<>();
  }

  private String classDesc(Class<?> c) {
    return c.getName().replace('.', '/');
  }

  private boolean isAnnotated(FieldNode field) {
    List<AnnotationNode> visibleAnnotations = field.visibleAnnotations;
    if (visibleAnnotations != null) {
      for (AnnotationNode annotationNode : visibleAnnotations) {
        if (annotationNode.desc.equals("Lbrennus/asm/specializer/Specialized;")) {
          return true;
        }
      }
    }
    return false;
  }

  private String getClassDesc(Class<?> c) {
    return TypeIdentifier.identifier(c).toString();
  }

  private Object get(Object o, String field) {
    try {
      Field f;
      f = o.getClass().getDeclaredField(field);
      f.setAccessible(true);
      return f.get(o);
    } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
  private MethodNode specializeMethod(final Map<String, FieldTranslation> originalDescToFieldTranslation, MethodNode method) {
    try (final Logger l = new Logger("specializeMethod(" + originalDescToFieldTranslation + ", " + method.signature + ")")) {
      //    l.log(originalClassName + " => " + specializedClassName);
      MethodNode specializedMethod = new MethodNode();
      specializedMethod.access = method.access;
      specializedMethod.name = method.name;
      specializedMethod.desc = method.desc;
      specializedMethod.signature = method.signature;
      //    
      specializedMethod.annotationDefault = method.annotationDefault;
      specializedMethod.attrs = method.attrs;
      specializedMethod.exceptions = method.exceptions;
      specializedMethod.invisibleAnnotations = method.invisibleAnnotations;
      specializedMethod.invisibleParameterAnnotations = method.invisibleParameterAnnotations;
      specializedMethod.localVariables = new ArrayList(); 
      //    specializedMethod.localVariables = method.localVariables;
      //    specializedMethod.maxLocals = method.maxLocals;
      //    specializedMethod.maxStack = method.maxStack;
      //    specializedMethod.localVariables = method.localVariables;
      //    specializedMethod.tryCatchBlocks = method.tryCatchBlocks;
      specializedMethod.visibleAnnotations = method.visibleAnnotations;
      specializedMethod.visibleParameterAnnotations = method.visibleParameterAnnotations;

      method.accept(new MethodAdapter(specializedMethod) {

        @Override
        public void visitMethodInsn(int inst, String className, String methodName, String signature) {
          l.log("meth " + className + "." + methodName + signature);
          super.visitMethodInsn(inst, className, methodName, signature);
        }
        @Override
        public void visitFieldInsn(int inst, String className, String fieldName, String signature) {
          l.log("field " + className + "." + fieldName + " " + signature);
          if (originalDescToFieldTranslation.containsKey(className)) {
            FieldTranslation fieldTranslation = originalDescToFieldTranslation.get(className);
            className = fieldTranslation.specializedDesc;
            if (fieldTranslation.fieldNameToField.containsKey(fieldName)) {
              signature = getClassDesc(fieldTranslation.fieldNameToField.get(fieldName).getSpecializedClass().getSpecializedClass());
            }
            l.log("-> changed to " + className + "." + fieldName + " " + signature);
          }
          super.visitFieldInsn(inst, className, fieldName, signature);
        }

      });

      return specializedMethod;
    }
  }

  public static class ClassDescriptor {
    
    private final ClassDescriptor parent;
    private final Class<?> originalClass;
    private final Class<?> specializedClass;
    private final Map<String, FieldDescriptor> fields;
    private final boolean specialized;
    
    public ClassDescriptor(ClassDescriptor parent, Class<?> originalClass, Class<?> specializedClass, LinkedHashMap<String, FieldDescriptor> fields, boolean specialized) {
      super();
      this.parent = parent;
      this.originalClass = originalClass;
      this.specializedClass = specializedClass;
      this.specialized = specialized;
      this.fields = Collections.unmodifiableMap(new LinkedHashMap<>(fields));
    }

    public Class<?> getSpecializedClass() {
      return this.specializedClass;
    }
    public ClassDescriptor getParent() {
      return parent;
    }
    public Map<String, FieldDescriptor> getFields() {
      return fields;
    }
    public boolean isSpecialized() {
      return specialized;
    }
  }
  

  public static class FieldDescriptor {

    private final String name;
    private final ClassDescriptor specializedClass;

    FieldDescriptor(String name, ClassDescriptor specializedClass) {
      super();
      this.name = name;
      this.specializedClass = specializedClass;
    }

    public ClassDescriptor getSpecializedClass() {
      return this.specializedClass;
    }
    public String getName() {
      return name;
    }
  }
  
  private static class FieldTranslation {
    final String originalDesc;
    final String specializedDesc;
    final Map<String, FieldDescriptor> fieldNameToField;
    public FieldTranslation(String originalDesc, String specializedDesc, Map<String, FieldDescriptor> fieldNameToField) {
      super();
      this.originalDesc = originalDesc;
      this.specializedDesc = specializedDesc;
      this.fieldNameToField = fieldNameToField;
    }
  }
}
