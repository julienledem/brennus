package brennus;

public enum Protection implements Keyword {
  Public, Protected, Default, Private;

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
    return tmpProtection == null ? Default : tmpProtection;
  }

}
