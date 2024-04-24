package com.difftool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileComparator {

  public static void main(String[] args) {
    String dir1 = "src/main/resources/version1"; // "src/main/resources/version1
    String dir2 = "src/main/resources/version2"; // "src/main/resources/version2

    List<String> files1 = getAllFiles(dir1);
    List<String> files2 = getAllFiles(dir2);

    List<String> commonFiles = new ArrayList<>();
    for (String file : files1) {
      if (files2.contains(file)) {
        commonFiles.add(file);
      }
    }

    List<String> addedFiles = new ArrayList<>(files2);
    addedFiles.removeAll(commonFiles);

    List<String> deletedFiles = new ArrayList<>(files1);
    deletedFiles.removeAll(commonFiles);

    System.out.println("Common Files:");
    System.out.println(commonFiles);

    System.out.println("Added Files:");
    System.out.println(addedFiles);

    System.out.println("Deleted Files:");
    System.out.println(deletedFiles);

    compareCommonFiles(dir1, dir2, commonFiles);
  }

  public static List<String> getAllFiles(String dir) {
    List<String> files = new ArrayList<>();
    File directory = new File(dir);

    if (!directory.exists()) {
      System.out.println("The directory " + dir + " does not exist.");
    } else if (!directory.isDirectory()) {
      System.out.println("The path " + dir + " is not a directory.");
    } else {
      System.out.println("The directory " + dir + " exists and is a valid directory.");
    }

    for (File file : directory.listFiles()) {
      if (file.isFile()) {
        files.add(file.getName());
      } else if (file.isDirectory()) {
        files.addAll(getAllFiles(file.getPath()));
      }
    }
    return files;
  }

  public static void compareCommonFiles(String dir1, String dir2, List<String> commonFiles) {
    for (String file : commonFiles) {
      String file1Path = new File(dir1, file).getPath();
      String file2Path = new File(dir2, file).getPath();

      try {
        String data1 = new String(Files.readAllBytes(new File(file1Path).toPath()));
        String data2 = new String(Files.readAllBytes(new File(file2Path).toPath()));

        if (data1.equals(data2)) {
          System.out.println("File " + file + " is identical in both directories.");
        } else {
          System.out.println("File " + file + " is different in both directories.");
          MayerAlgo mayerAlgo = new MayerAlgo(file1Path, file2Path);

          // FileDiffTool diffTool = new FileDiffTool();
          // try {
          // List<String> differences = diffTool.compareFiles(file1Path, file2Path);
          // System.out.println("Differences:");
          // int originalLineNum = 1;
          // int modifiedLineNum = 1;

          // for (String diff : differences) {
          // if (diff.startsWith("-")) {
          // System.out.printf("[%d] %s\n", originalLineNum, diff);
          // originalLineNum++;
          // } else if (diff.startsWith("+")) {
          // System.out.printf("[%d] %s\n", modifiedLineNum, diff);
          // modifiedLineNum++;
          // }
          // }
          // } catch (Exception e) {
          // System.out.println("Error reading files: " + e.getMessage());
          // }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
