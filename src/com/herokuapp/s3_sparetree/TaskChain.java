package com.herokuapp.s3_sparetree;

import java.util.ArrayList;
import java.util.List;

public class TaskChain {
  private Tree srcTree;
  private Tree tgtTree;
  private List tasks = new ArrayList<Task>();

  public TaskChain(Tree srcTree, Tree tgtTree) {
    this.srcTree = srcTree;
    this.tgtTree = tgtTree;
  }

  public void buildAndRun() {
    new WalkForward().walk(srcTree, tgtTree, tasks);
    new WalkBack().walk(srcTree, tgtTree, tasks);
    run();
  }

  private void run() {
    System.out.println("TaskChain has " + tasks.size() + " items.");
    for (Object obj : tasks) {
      ((Task) obj).run();
    }
  }
}