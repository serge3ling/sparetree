package com.herokuapp.s3_sparetree;

import java.io.File;

public class Knot implements Comparable {
  private File data;
  private String path = "";
  //private Knot[] knots;

  public Knot(File data, Knot parentKnot) {
    this.data = data;
    if (parentKnot != null) {
      path = parentKnot.relativePath() + File.separator + parentKnot.getData().getName();
    }
  }

  @Override
  public boolean equals(Object otherObject) {
    if (otherObject == this) {
        return true;
    }
    if (!(otherObject instanceof Knot)) {
      return false;
    }
    return data.equals(((Knot) otherObject).getData());
  }

  @Override
  public int compareTo(Object otherObject) {
    Knot other = (Knot) otherObject;
    int cmp = compareParentTo(other);
    boolean goOn = (cmp == 0) && !data.equals(other.getData());

    if (goOn) {
      if (data.isDirectory() && other.getData().isDirectory()
          || data.isFile() && other.getData().isFile()
          ) {
        cmp = data.compareTo(other.getData());
        goOn = false;
      }
    }

    if (goOn) {
      cmp = data.isFile() ? 1 : (-1);
    }

    return cmp;
  }

  private int compareParentTo(Knot other) {
    String thisParent = data.getParent();
    String otherParent = other.getData().getParent();
    int cmp = 0;
    boolean goOn = ((thisParent != null) || (otherParent != null));

    if (goOn) {
      if ((thisParent != null) && (otherParent != null)) {
        cmp = thisParent.compareTo(otherParent);
        goOn = false;
      }
    }

    if (goOn) {
      cmp = (thisParent == null) ? -1 : 1;
    }

    return cmp;
  }

  public File getData() {
    return data;
  }

  @Override
  public String toString() {
    return path + ", " + data.toString();
  }

  public String relativePath() {
    return path;
  }
}