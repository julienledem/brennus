package brennus;

import brennus.model.ExistingType;
import brennus.model.Type;

/**
 * The starting point to generate a Java class
 *
 * @author Julien Le Dem
 *
 */
final public class Builder {

  private final boolean generateLineNumbers;

  int getSourceLineNumber() {
    if (generateLineNumbers) {
      StackTraceElement creatingStackFrame = getCreatingStackFrame();
      return creatingStackFrame!=null && creatingStackFrame.getLineNumber()>0 ? creatingStackFrame.getLineNumber() : 0;
    } else {
      return 0;
    }
  }

  StackTraceElement getCreatingStackFrame() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    String builderPackageName = MethodContext.class.getPackage().getName();
    for (StackTraceElement stackTraceElement : stackTrace) {
      String className = stackTraceElement.getClassName();
      String packageName = className.substring(0, className.lastIndexOf("."));
      if (!packageName.startsWith("java.")
          && !packageName.equals(builderPackageName)
          ) {
        return stackTraceElement;
      }
    }
    return null;
  }

  public Builder() {
    this(true);
  }

  public Builder(boolean generateLineNumbers) {
    this.generateLineNumbers = generateLineNumbers;
  }

  /**
   * startClass(name, extending)[.[static]field(protection, type, name)]*[.start[Static]Method(protection, return, name){statements}.endMethod()]*.endClass()
   * @param name the fully qualified name of the class
   * @return a ClassBuilder
   */
  public ClassBuilder startClass(String name, Type extending) {
    return new ClassBuilder(name, extending, this);
  }

  /**
   * startClass(name, extending)[.[static]field(protection, type, name)]*[.start[Static]Method(protection, return, name){statements}.endMethod()]*.endClass()
   * @param name the fully qualified name of the class
   * @return a ClassBuilder
   */
  public ClassBuilder startClass(String name) {
    return new ClassBuilder(name, ExistingType.OBJECT, this);
  }

}
