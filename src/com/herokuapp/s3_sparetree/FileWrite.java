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
    this.srcKnot = srcKnot;
    this.src = srcKnot.getData();
    this.tgt = new File(rootData.getPath() + File.separator
        + srcKnot.relativePath() + File.separator + src.getName());
  }

  @Override
  public void run() {
    if (srcKnot.isLeaf()) {
      copy();
    } else {
      mkdir();
    }
  }

  public void mkdir() {
    tgt.mkdirs();
  }

  public void copy() {
    InputStream srcStream = null;
    OutputStream tgtStream = null;
    try {
      srcStream = new FileInputStream(src);
      tgtStream = new FileOutputStream(tgt);
      byte[] buffer = new byte[4096];
      int length = -1;
      while ((length = srcStream.read(buffer)) > 0) {
        tgtStream.write(buffer, 0, length);
      }
      System.out.println("[ OK ] Copy " + src + " to " + tgt);
    } catch (IOException e) {
      System.err.println("[FAIL] Copy " + src + " to " + tgt);
    } finally {
      try {
        srcStream.close();
        tgtStream.close();
        tgt.setLastModified(src.lastModified());
      } catch (Exception e) {
      }
    }
  }
}