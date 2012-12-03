package brennus.model;

import static brennus.model.Protection.DEFAULT;
import static brennus.model.Protection.PRIVATE;
import static brennus.model.Protection.PROTECTED;
import static brennus.model.Protection.PUBLIC;
import static brennus.model.StaticStatus.STATIC;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class MemberFlags {

  private final Keyword[] keywords;

  private final boolean isStatic;
  private final Protection protection;

  public MemberFlags(Keyword[] keywords) {
    this.keywords = keywords;
    this.protection = Protection.getProtection(keywords);
    this.isStatic = StaticStatus.isStatic(keywords);
  }

  public MemberFlags(boolean isStatic, Protection protection) {
    this.keywords = isStatic ? new Keyword[] { STATIC, protection } : new Keyword[] { protection };
    this.protection = protection;
    this.isStatic = isStatic;
  }

  public Protection getProtection() {
    return protection;
  }

  public boolean isStatic() {
    return isStatic;
  }

  Keyword[] getKeywords() {
    return keywords;
  }

  public static MemberFlags fromKeywords(Keyword... keywords) {
    return new MemberFlags(keywords);
  }

  public static MemberFlags fromReflection(java.lang.reflect.Method method) {
    int modifiers = method.getModifiers();
    Protection protection = getProtection(modifiers);
    return new MemberFlags(Modifier.isStatic(modifiers), protection);
  }

  public static MemberFlags fromReflection(Constructor<?> constructor) {
    Protection protection = getProtection(constructor.getModifiers());
    return new MemberFlags(false, protection);
  }

  private static Protection getProtection(int modifiers) {
    Protection protection;
    if (Modifier.isPublic(modifiers)) {
      protection = PUBLIC;
    } else if (Modifier.isPrivate(modifiers)) {
      protection = PRIVATE;
    } else if (Modifier.isProtected(modifiers)) {
      protection = PROTECTED;
    } else {
      protection = DEFAULT;
    }
    return protection;
  }

}
