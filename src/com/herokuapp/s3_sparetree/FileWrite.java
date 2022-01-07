package com.herokuapp.s3_sparetree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileWrite implements Task {
  private Knot srcKnot;
  private File src;
  private File tgt;

  public FileWrite(Knot srcKnot, File rootData) {
    System.out.println(rootData.getAbsolutePath());
    this.srcKnot = srcKnot;
    this.src = srcKnot.getData();
    this.tgt = new File(rootData.getPath() + File.separator + srcKnot.relativePath() + File.separator + src.getName());
  }

  @Override
  public void run() {
    if (srcKnot.isDirectory()) {
      mkdir();
    } else {
      copy();
    }
  }

  public void mkdir() {
    tgt.mkdirs();
  }

  public void copy() {
    InputStream input = null;
    OutputStream output = null;
    try {
      input = new FileInputStream(src);
      output = new FileOutputStream(tgt);
      byte[] buffer = new byte[4096];
      int length = -1;
      while ((length = input.read(buffer)) > 0) {
        output.write(buffer, 0, length);
      }
    } catch (IOException e) {
      System.err.println("[FAIL] Copy " + src + " to " + tgt);
    } finally {
      try {
        input.close();
        output.close();
      } catch (Exception e) {
      }
    }
  }
}