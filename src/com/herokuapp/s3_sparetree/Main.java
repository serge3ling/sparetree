package com.herokuapp.s3_sparetree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Main {
  private List<File> files = new ArrayList<>();
  
  public static void main(String[] args) {
    Tree srcTree = new Tree(new File("src"));
    srcTree.build();
    srcTree.print();

    Tree tgtTree = new Tree(new File("t"));
    tgtTree.build();
    tgtTree.print();

    TaskChain chain = new TaskChain(srcTree, tgtTree);
    chain.buildAndRun();
  }
  
  public Main(File root) {
    listFiles(root);
  }
  
  public void listFiles(final File dir) {
    for (final File file: dir.listFiles()) {
      files.add(file);
      if (file.isDirectory()) {
        listFiles(file);
      }
    }
  }
  
  public void print() {
    ListIterator iterator = files.listIterator(files.size());
    File previous = null;
    while (iterator.hasPrevious()) {
      File file = (File) iterator.previous();
      System.out.println("" + (previous == null ? " " : file.compareTo(previous)) + "\t" + file);
      previous = file;
    }
  }
}
