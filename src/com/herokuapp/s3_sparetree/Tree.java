package com.herokuapp.s3_sparetree;

import java.io.File;
import java.util.TreeSet;

public class Tree {
  private File root;
  private TreeSet<Knot> knots = new TreeSet<>();

  public Tree(File root) {
    this.root = root;
  }

  public void build() {
    readKnots(root, null);
  }

  private void readKnots(final File dir, Knot parentKnot) {
    for (final File file: dir.listFiles()) {
      Knot knot = new Knot(file, parentKnot);
      knots.add(knot);
      if (file.isDirectory()) {
        readKnots(file, knot);
      }
    }
  }

  public TreeSet<Knot> getKnots() {
    return knots;
  }

  public void print() {
    for (final Knot knot: knots) {
      System.out.println(knot);
    }
  }
}