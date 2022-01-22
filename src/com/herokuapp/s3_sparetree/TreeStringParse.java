package com.herokuapp.s3_sparetree;

public class TreeStringParse {
  public static final String COUPLE_SEPARATOR = "->";
  public static final int COUPLE_SEPARATOR_LENGTH = COUPLE_SEPARATOR.length();

  private String srcString = "";
  private String tgtString = "";

  TreeStringParse(String unparsed) {
    int ix = unparsed.indexOf(COUPLE_SEPARATOR);
    if (ix > 0) {
      srcString = unparsed.substring(0, ix).trim();
      tgtString = unparsed.substring(ix + COUPLE_SEPARATOR_LENGTH).trim();
    }
  }

  public String src() {
    return srcString;
  }

  public String tgt() {
    return tgtString;
  }
}