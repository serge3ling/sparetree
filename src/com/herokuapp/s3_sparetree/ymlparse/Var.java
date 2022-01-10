package com.herokuapp.s3_sparetree.ymlparse;

import java.util.Objects;

public class Var {
  private String name;
  private boolean typeSet;
  private VarType type;
  private StringBuffer valBuffer = new StringBuffer();
  private Object val;
  private boolean built;

  public Var(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public VarType getType() {
    return type;
  }

  public void setType(VarType type) throws YmlParseException {
    if (typeSet) {
      throw new YmlParseException("Var type can be set only once. Var name: {0}.", name);
    } else {
      this.type = type;
      typeSet = true;
    }
  }

  public void appendToBuilder(String s) {
    valBuffer.append(s);
  }

  public void build() throws YmlParseException {
    if (!built) {
      built = true;
      switch (type) {
        case TXT:
          val = valBuffer.toString();
          break;
        case BRACKET_ARR:
          buildBracketArray();
          type = VarType.ARR;
          break;
        case HYPHEN_ARR:
          buildHyphenArray();
          type = VarType.ARR;
          break;
        default:
      }
    }
  }

  void buildBracketArray() {
    buildArray(",");
  }

  void buildHyphenArray() {
    buildArray("\n");
  }

  void buildArray(String separator) {
    String[] separated = valBuffer.toString().split(separator);
    val = new String[separated.length];
    for (int i = 0; i < separated.length; i++) {
      ((String[]) val)[i] = separated[i].trim();
    }
  }

  public Object getVal() {
    return val;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Var var = (Var) o;
    return Objects.equals(name, var.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    String s = "";
    switch (type) {
      case TXT:
        s = val.toString();
        break;
      case BRACKET_ARR:
      case HYPHEN_ARR:
      case ARR:
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < ((String[]) val).length; i++) {
          sb.append(((String[]) val)[i]).append(",\n");
        }
        sb.append("]");
        s = sb.toString();
        break;
      default:
    }
    return name + " (" + type + "): " + s;
  }
}
