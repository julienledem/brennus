package brennus.model;

public final class Field {

  private final MemberFlags flags;
  private final Type type;
  private final String name;

  public Field(MemberFlags flags, Type type, String name) {
    this.flags = flags;
    this.type = type;
    this.name = name;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public boolean isStatic() {
    return flags.isStatic();
  }

  public MemberFlags getFlags() {
    return flags;
  }

  public String getSignature() {
    return type.getSignature();
  }

}
