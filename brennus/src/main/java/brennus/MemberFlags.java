package brennus;

public class MemberFlags {

  private final Keyword[] keywords;

  private final boolean isStatic;
  private final Protection protection;

  MemberFlags(Keyword[] keywords) {
    this.keywords = keywords;
    this.protection = Protection.getProtection(keywords);
    this.isStatic = StaticStatus.isStatic(keywords);
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

}
