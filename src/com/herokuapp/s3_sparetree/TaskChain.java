package com.herokuapp.s3_sparetree;

import java.util.ArrayList;
import java.util.List;

public class TaskChain {
  private Tree tree;
  private List tasks = new ArrayList<Task>();

  public TaskChain(Tree tree) {
    this.tree = tree;
  }

  public void buildAndRun() {
    new WalkForward().walk(tree.getKnots(), tasks);
    new WalkBack().walk(tree.getKnots(), tasks);
    run();
  }

  private void run() {
    ;
  }
}