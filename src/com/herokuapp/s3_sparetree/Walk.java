package com.herokuapp.s3_sparetree;

import java.util.List;
import java.util.Set;

public interface Walk {
  void walk(Set<Knot> srcKnots, Set<Knot> tgtKnots, List<Task> tasks);
}