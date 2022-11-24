package egon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class nvloc_egon {

    public static int nvloc (String directory) {
        File file = new File(directory);
        if (file.isDirectory() || !file.getName().endsWith(".java")) {
            return 0;
        }
        int lineCount = (new LineCounter(file)).getCount();
        return lineCount;
    }
}
class LineCounter {
    File file;
    int lineCount;

    public LineCounter (File f) {
        file = f;
        lineCount = -1;
    }

    public int getCount() {
        return lineCount > -1 ? lineCount : countLines();
    }

    private int countLines () {
        lineCount = 0;
        try (Scanner scanner = new Scanner(file)) {
            boolean multiline = false;
            boolean keepLast = false;
            boolean doubleCount = false;
            String line = "";
            while (scanner.hasNextLine() || keepLast) {
                if (!keepLast) {
                    line = scanner.nextLine().strip();
                    doubleCount = false;
                } else {
                    keepLast = false;
                }

                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("//")) {
                    continue;
                }
                if (line.matches(".*/\\*.*")) {
                    multiline = true;
                    String[] substrings = line.split("/\\*", 2);
                    if (substrings[0].strip().length() > 0 && !doubleCount) {
                        // there is code before the comment
                        lineCount++;
                        // no double counting
                        doubleCount = true;
                    }
                    // read rest of line in next iteration
                    line = substrings[1].strip();
                    keepLast = true;
                    continue;
                }
                if (multiline) {
                    if (line.matches(".*\\*/.*")) {
                        String[] substrings = line.split("\\*/", 2);
                        // read rest of line in next iteration
                        line = substrings[1].strip();
                        keepLast = true;
                        // no longer in comment
                        multiline = false;
                    }
                    continue;
                }
                // not a comment, increment count
                if (!doubleCount) {
                    lineCount++;
                }
            }
        } catch (FileNotFoundException ignored) {
            // don't care
        }
        return lineCount;
    }
}
