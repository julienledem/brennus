package brennus;

public enum StaticStatus implements Keyword {

  Static;

  public static boolean isStatic(Keyword... keywords) {
    for (Keyword k : keywords) {
      if (k == Static) {
        return true;
      }
    }
    return false;
  }

}
