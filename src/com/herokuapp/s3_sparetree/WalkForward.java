package com.herokuapp.s3_sparetree;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WalkForward {
  private File tgtRootData;
  private Iterator srcIterator;
  private Iterator tgtIterator;
  private List<Task> tasks;
  private Knot srcKnot;
  private Knot tgtKnot;
  private boolean hasTgtKnot;

  public void walk(Tree srcTree, Tree tgtTree, List<Task> tasks) {
    tgtRootData = tgtTree.getRootData();
    srcIterator = srcTree.getKnots().iterator();
    tgtIterator = tgtTree.getKnots().iterator();
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
    hasTgtKnot = tgtIterator.hasNext();
    if (hasTgtKnot) {
      tgtKnot = (Knot) tgtIterator.next();
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
    // I think, needed arguments are srcKnot and tgtRootData.
    tasks.add(new FileWrite(srcKnot, tgtRootData));
    //System.out.println("src " + srcKnot);
  }

  private boolean filesSeemDifferent() {
    boolean different = true;

    if (srcKnot.isDirectory() && tgtKnot.isDirectory()) {
      different = false;
    }

    if (different) {
      if (srcKnot.isLeaf() && tgtKnot.isLeaf()) {
        if (srcKnot.dataEqual(tgtKnot)) {
          different = false;
        }
      } else {
        different = false;
      }
    }

    return different;
  }
}