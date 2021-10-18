package com.herokuapp.s3_sparetree;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WalkForward {
  private Iterator srcIterator;
  private Iterator tgtIterator;

  public void walk(Set<Knot> srcKnots, Set<Knot> tgtKnots, List<Task> tasks) {
    srcIterator = srcKnots.iterator();
    tgtIterator = tgtKnots.iterator();

    while (srcIterator.hasNext()) {
      Knot srcKnot = (Knot) srcIterator.next();
    }
  }
}