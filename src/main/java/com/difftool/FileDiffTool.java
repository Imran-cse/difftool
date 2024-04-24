package com.difftool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDiffTool {
  public List<String> compareFiles(String originalFile, String modifiedFile) throws IOException {
    List<String> originalLines = readLinesFromFile(originalFile);
    List<String> modifiedLines = readLinesFromFile(modifiedFile);

    return computeDiff(originalLines, modifiedLines);
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

  private static List<String> computeDiff(List<String> originalLines, List<String> modifiedLines) {
    int[][] dp = new int[originalLines.size() + 1][modifiedLines.size() + 1];

    for (int i = 1; i <= originalLines.size(); i++) {
      for (int j = 1; j <= modifiedLines.size(); j++) {
        if (originalLines.get(i - 1).equals(modifiedLines.get(j - 1))) {
          dp[i][j] = dp[i - 1][j - 1] + 1;
        } else {
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }

    List<String> differences = new ArrayList<>();
    int i = originalLines.size();
    int j = modifiedLines.size();

    while (i > 0 && j > 0) {
      if (originalLines.get(i - 1).equals(modifiedLines.get(j - 1))) {
        i--;
        j--;
      } else if (dp[i - 1][j] >= dp[i][j - 1]) {
        differences.add("- " + originalLines.get(i - 1)); // Line removed
        i--;
      } else {
        differences.add("+ " + modifiedLines.get(j - 1)); // Line added
        j--;
      }
    }

    while (i > 0) {
      differences.add("- " + originalLines.get(i - 1)); // Remaining lines in original
      i--;
    }

    while (j > 0) {
      differences.add("+ " + modifiedLines.get(j - 1)); // Remaining lines in modified
      j--;
    }

    return differences;
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

      List<String> differences = computeDiff(originalLines, modifiedLines);

      System.out.println("Differences:");
      int originalLineNum = 1;
      int modifiedLineNum = 1;

      for (String diff : differences) {
        if (diff.startsWith("-")) {
          System.out.printf("[%d] %s\n", originalLineNum, diff);
          originalLineNum++;
        } else if (diff.startsWith("+")) {
          System.out.printf("[%d] %s\n", modifiedLineNum, diff);
          modifiedLineNum++;
        }
      }
    } catch (IOException e) {
      System.out.println("Error reading files: " + e.getMessage());
    }
  }
}
