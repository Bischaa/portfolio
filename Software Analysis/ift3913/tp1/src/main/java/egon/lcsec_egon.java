package egon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lcsec_egon {

    public static int[] lcsec(String directory, ArrayList<String[]> files) {
        int count = 0;
        // parse java files for class names
        Path rootDir = Path.of(directory);
        int[] scores = new int[files.size()];
        for (int currentIndex = 0; currentIndex < files.size(); currentIndex++) {
            String classname = files.get(currentIndex)[2];

            for (int searchIndex = 0; searchIndex < files.size(); searchIndex++) {
                if (currentIndex != searchIndex) {
                    String searchPath = files.get(searchIndex)[0];

                    // from rootDir we resolve the absolute path of searchFile
                    File searchFile = rootDir.resolve(Path.of(searchPath)).toFile();
                    count = getCSEC(searchFile, classname);

                    // add count to both classes
                    scores[currentIndex] += count;
                    scores[searchIndex] += count;
                }
            }
        }
        return scores;
    }
    public static int getCSEC(File file, String classname) {
        int count = 0;
        Pattern pattern = Pattern.compile("\\b" + classname + "\\b");

        try (Scanner scanner = new Scanner(file)) {
            boolean multiline = false;
            boolean keepLast = false;
            String line = "";
            while (scanner.hasNextLine() || keepLast) {
                if (!keepLast) {
                    line = scanner.nextLine().strip();
                } else {
                    keepLast = false;
                }

                if (line.length() == 0) {
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
                if (line.startsWith("//")) {
                    continue;
                }
                if (line.matches(".*/\\*.*")) {
                    multiline = true;
                    String[] substrings = line.split("/\\*", 2);
                    if (substrings[0].strip().length() > 0) {
                        // there is code before the comment
                        Matcher matcher = pattern.matcher(substrings[0]);
                        while (matcher.find()) {
                            count++;
                        }
                    }
                    // read rest of line in next iteration
                    line = substrings[1].strip();
                    keepLast = true;
                    continue;
                }
                // not a comment, check line for method
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    count++;
                }
            }
        } catch (FileNotFoundException ignored) {
            // don't care
        }

        return count;
    }
}
