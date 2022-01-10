package com.herokuapp.s3_sparetree;

public class FileDelete implements Task {
	private Knot knot;

  public FileDelete(Knot knot) {
    this.knot = knot;
  }

  @Override
  public void run() {
    try {
      if (knot.getData().delete()) {
        System.out.println("[ OK ] rm " + knot.getData());
      } else {
        System.err.println("[FAIL] rm " + knot.getData());
      }
    } catch (SecurityException e) {
      System.err.println("[FAIL] rm " + knot.getData());
    }
  }
}