package lcsec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lcsec {

    public static void main(String[] args) throws IOException {
        // read arguments
        if (args.length < 2) {
            throw new IOException("Not enough arguments");
        }
        String directory = args[0];
        File csv = new File(args[1]);

        // extract info from csv to files
        // path in line[0], pkg in line[1], file/classname in line[2]
        ArrayList<String[]> files = new ArrayList<>();
        Scanner scan = new Scanner(csv);
        scan.useDelimiter("\n");
        while (scan.hasNext()) {
            String nextLine = scan.next();
            // strip leading and trailing whitespace
            String[] entry = Arrays.stream(nextLine.split(",")).map(String::strip).toArray(String[]::new);
            files.add(entry);
        }
        scan.close();

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
                    int count = getCSEC(searchFile, classname);

                    // add count to both classes
                    scores[currentIndex] += count;
                    scores[searchIndex] += count;
                }
            }
        }

        // print final output
        for (int i = 0; i < files.size(); i++) {
            String[] entry = files.get(i);
            System.out.println(entry[0] + ", " + entry[1] + ", " + entry[2] + ", " + scores[i]);
        }

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