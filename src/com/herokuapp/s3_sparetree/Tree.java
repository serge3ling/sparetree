package com.herokuapp.s3_sparetree;

import java.io.File;
import java.util.TreeSet;

public class Tree {
  private File rootData;
  private TreeSet<Knot> knots = new TreeSet<>();

  public Tree(File rootData) {
    this.rootData = rootData;
  }

  public void build() {
    readKnots(rootData, null);
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

  public File getRootData() {
    return rootData;
  }

  public void print() {
    System.out.println("rootData: " + rootData + ". Items: " + knots.size() + ".");
    for (final Knot knot: knots) {
      System.out.println(knot);
    }
  }
}