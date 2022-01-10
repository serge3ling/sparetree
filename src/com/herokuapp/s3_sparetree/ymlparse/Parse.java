package com.herokuapp.s3_sparetree.ymlparse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parse {
  enum State {
    FIND_VAR,
    FIND_TYPE,
    READ_VAR
  }

  private String fileName = "job.yml";
  private State state = State.FIND_VAR;
  private Var vr;
  private final Map<String, Var> map = new HashMap<>();

  public Parse(String fileName) {
    this();
    this.fileName = fileName;
  }

  public Parse() {
  }

  public void readFile() throws YmlParseException {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line = "";
      while (line != null) {
        String trimmed = line.trim();
        if (!(trimmed.length() == 0) && !(trimmed.startsWith("#"))) {
          parseLine(trimmed);
        }
        line = reader.readLine();
      }
      if (vr != null) {
        vr.build();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    printMap();
  }

  void parseLine(String line) throws YmlParseException {
    switch (state) {
      case FIND_VAR:
        findVar(line);
        break;
      case FIND_TYPE:
        findType(line);
        break;
      case READ_VAR:
        readVar(line);
        break;
      default:
    }
  }

  void findVar(String line) throws YmlParseException {
    int hashIndex = line.indexOf(" #");
    int colonIndex = line.indexOf(": ");
    if ((hashIndex >= 0) && (hashIndex < colonIndex)) {
      throw new YmlParseException("Meaningless statement before # sign: \"{0}\".", line);
    }
    if (colonIndex < 1) {
      if ((line.length() > 1) && (line.charAt(line.length() - 1) == ':')) {
        colonIndex = line.length() - 1;
      } else {
        throw new YmlParseException("Meaningless statement: \"{0}\".", line);
      }
    }

    String noComment = (hashIndex > 0) ? line.substring(hashIndex + 2) : line;

    String name = noComment.substring(0, colonIndex).trim();
    String pattern = "^[a-z_][a-z_0-9\\-]+$";
    if (!name.matches(pattern)) {
      throw new YmlParseException("Wrong var name: \"{0}\".", line);
    }

    String tail = noComment.substring(colonIndex + 1).trim();
    vr = new Var(name);
    map.put(name, vr);
    if (tail.length() > 0) {
      if (tail.charAt(0) == '[') {
        vr.setType(VarType.BRACKET_ARR);
        parseBracketArray(tail.substring(1));
      } else {
        vr.setType(VarType.TXT);
        vr.appendToBuilder(tail);
        vr.build();
      }
    } else {
      state = State.FIND_TYPE;
    }
  }

  void findType(String line) throws YmlParseException {
    boolean found = false;
    if (line.charAt(0) == '[') {
      found = true;
      vr.setType(VarType.BRACKET_ARR);
      parseBracketArray(line);
    }
    if (line.charAt(0) == '-') {
      found = true;
      vr.setType(VarType.HYPHEN_ARR);
      state = State.READ_VAR;
      parseHyphenRow(line);
    }
    if (!found) {
      throw new YmlParseException("Unrecognized type of var \"{0}\".", vr.getName());
    }
  }

  void parseHyphenRow(String line) {
    String noHyphen = line.substring(1).trim();
    int hashIndex = noHyphen.indexOf(" #");
    String noComment = ((hashIndex > 0) ? noHyphen.substring(hashIndex + 2) : noHyphen).trim();
    vr.appendToBuilder(noComment);
  }

  void parseBracketArray(String tail) throws YmlParseException {
    int closing = tail.indexOf(']');

    if (closing >= 0) {
      if (closing == (tail.length() - 1)) {
        if (closing > 0) {
          vr.appendToBuilder(tail.substring(1, closing).trim());
        }
        vr.build();
        state = State.FIND_VAR;
      } else {
        throw new YmlParseException("After ']' closing array there cannot be any text: \"{0}\".", tail);
      }
    } else {
      state = State.READ_VAR;
      vr.appendToBuilder(" " + tail.trim());
    }
  }

  void readVar(String line) throws YmlParseException {
    switch (vr.getType()) {
      case BRACKET_ARR:
        parseBracketArray(line);
        break;
      case HYPHEN_ARR:
        if (line.charAt(0) == '-') {
          vr.appendToBuilder("\n");
          parseHyphenRow(line);
        } else {
          vr.build();
          state = State.FIND_VAR;
          findVar(line);
        }
        break;
      default:
    }
  }

  void printMap() {
    for (Var mapVar: map.values()) {
      System.out.println(mapVar);
    }
  }

  public Object getVal(String name, VarType type) {
    Var mapVar = map.get(name);
    Object obj = null;
    if (mapVar != null) {
      obj = mapVar.getVal();
    }
    return obj;
  }

  public String getTxt(String name) {
    String s = null;
    Var mapVar = map.get(name);
    if ((mapVar != null) && (mapVar.getType() == VarType.TXT)) {
      s = mapVar.getVal().toString();
    }
    return s;
  }

  public String[] getArr(String name) {
    String[] s = null;
    Var mapVar = map.get(name);
    if ((mapVar != null) && (mapVar.getType() == VarType.ARR)) {
      s = (String[]) mapVar.getVal();
    }
    return s;
  }
}
