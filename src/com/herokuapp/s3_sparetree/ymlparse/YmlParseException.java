package com.herokuapp.s3_sparetree.ymlparse;

public class YmlParseException extends Exception {
  public YmlParseException(String message, String replacement) {
    this(message.replaceAll("\\Q{0}\\E", replacement));
  }

  public YmlParseException(String message) {
    super(message);
  }
}
