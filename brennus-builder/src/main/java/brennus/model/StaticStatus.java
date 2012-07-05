package brennus.model;

public enum StaticStatus implements Keyword {

  STATIC;

  public static boolean isStatic(Keyword... keywords) {
    for (Keyword k : keywords) {
      if (k == STATIC) {
        return true;
      }
    }
    return false;
  }

}
