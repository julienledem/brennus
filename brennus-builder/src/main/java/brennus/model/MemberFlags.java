package brennus.model;

import static brennus.model.StaticStatus.STATIC;

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
    // TODO: handle existingMethod
    return new MemberFlags(new Keyword[0]);
  }

}
