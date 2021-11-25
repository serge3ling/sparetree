package com.herokuapp.s3_sparetree;

import java.io.File;

public class FileWrite implements Task {
	private Knot srcKnot;
  private File rootData;

  public FileWrite(Knot srcKnot, File rootData) {
    this.srcKnot = srcKnot;
    this.rootData = rootData;
  }

  @Override
  public void run() {
    ;
  }
}