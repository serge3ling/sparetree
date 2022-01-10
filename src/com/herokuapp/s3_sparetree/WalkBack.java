package com.herokuapp.s3_sparetree;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WalkBack {
  private List<Task> tasks;
  private Iterator srcIterator;
  private Iterator tgtIterator;
  private Knot srcKnot;
  private Knot tgtKnot;
  private boolean hasSrcKnot;
  
  public List<Task> walk(Tree srcTree, Tree tgtTree, List<Task> tasks) {
    srcIterator = srcTree.getKnots().descendingIterator();
    tgtIterator = tgtTree.getKnots().descendingIterator();
    this.tasks = tasks;
    nextSrc();

    while (tgtIterator.hasNext()) {
      tgtKnot = (Knot) tgtIterator.next();

      if (hasSrcKnot) {
        whenHasSrcKnot();
      } else {
        enqueueDelete();
      }
    }

    return this.tasks;
  }

  private void nextSrc() {
    hasSrcKnot = srcIterator.hasNext();
    if (hasSrcKnot) {
      srcKnot = (Knot) srcIterator.next();
    }
  }

  private void whenHasSrcKnot() {
    int compared = tgtKnot.compareTo(srcKnot);
    if (compared > 0) {
      enqueueDelete();
    } else if (compared == 0) {
      nextSrc();
    } else if (compared < 0) {
      while (hasSrcKnot && (compared < 0)) {
        nextSrc();
        compared = tgtKnot.compareTo(srcKnot);
      }
    }
  }

  private void enqueueDelete() {
    tasks.add(new FileDelete(tgtKnot));
  }
}