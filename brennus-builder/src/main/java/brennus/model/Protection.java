package brennus.model;

public enum Protection implements Keyword {
  PUBLIC, PROTECTED, DEFAULT, PRIVATE;

  public static Protection getProtection(Keyword... keywords) {
    Protection tmpProtection = null;
    for (Keyword k : keywords) {
      if (k instanceof Protection) {
        if (tmpProtection != null) {
          throw new RuntimeException("more than one protection keyword: "+tmpProtection+" and "+k);
        }
        tmpProtection = (Protection)k;
      }
    }
    return tmpProtection == null ? DEFAULT : tmpProtection;
  }

}
