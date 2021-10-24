package com.herokuapp.s3_sparetree;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WalkForward {
  private Iterator srcIterator;
  private Iterator tgtIterator;
  private List<Task> tasks;
  private Knot srcKnot;
  private Knot tgtKnot;
  private boolean hasTgtKnot;

  public void walk(Set<Knot> srcKnots, Set<Knot> tgtKnots, List<Task> tasks) {
    srcIterator = srcKnots.iterator();
    tgtIterator = tgtKnots.iterator();
    this.tasks = tasks;
    nextTgt();

    while (srcIterator.hasNext()) {
      srcKnot = (Knot) srcIterator.next();

      if (hasTgtKnot) {
        whenHasTgtKnot();
      } else {
        enqueueWrite();
      }
    }
  }

  private void nextTgt() {
    if (tgtIterator.hasNext()) {
      tgtKnot = (Knot) tgtIterator.next();
      hasTgtKnot = true;
    } else {
      hasTgtKnot = false;
    }
  }

  private void whenHasTgtKnot() {
    int compared = srcKnot.compareTo(tgtKnot);
    if (compared < 0) {
      enqueueWrite();
    } else if (compared == 0) {
      if (filesSeemDifferent()) {
        enqueueWrite();
      }
      nextTgt();
    } else {
      while (hasTgtKnot) {
        nextTgt();
      }
    }
  }

  private void enqueueWrite() {
    //System.out.println("src " + srcKnot);
  }

  private boolean filesSeemDifferent() {
    // stub
    return true;
  }
}