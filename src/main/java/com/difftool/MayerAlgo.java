package com.difftool;

import com.github.difflib.*;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.AbstractDelta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MayerAlgo {

  public MayerAlgo(String file1Path, String file2Path) {
    try {
      List<String> originalLines = readLinesFromFile(file1Path);
      List<String> modifiedLines = readLinesFromFile(file2Path);

      Patch<String> patch = DiffUtils.diff(originalLines, modifiedLines);

      System.out.println("Differences:");
      for (AbstractDelta<String> delta : patch.getDeltas()) {
        System.out.println(delta);
      }
    } catch (IOException e) {
      System.out.println("Error reading files: " + e.getMessage());
    }
  }

  private static List<String> readLinesFromFile(String filename) throws IOException {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    }
    return lines;
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: java FileDiffTool <original_file> <modified_file>");
      return;
    }

    String originalFile = args[0];
    String modifiedFile = args[1];

    try {
      List<String> originalLines = readLinesFromFile(originalFile);
      List<String> modifiedLines = readLinesFromFile(modifiedFile);

      Patch<String> patch = DiffUtils.diff(originalLines, modifiedLines);

      System.out.println("Differences:");
      for (AbstractDelta<String> delta : patch.getDeltas()) {
        System.out.println(delta);
      }
    } catch (IOException e) {
      System.out.println("Error reading files: " + e.getMessage());
    }
  }
}
