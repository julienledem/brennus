package brennus.model;

public enum FinalStatus implements Keyword {

  FINAL;

  public static boolean isFinal(Keyword... keywords) {
    for (Keyword k : keywords) {
      if (k == FINAL) {
        return true;
      }
    }
    return false;
  }
}
