package jls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class jls {
    public static void main(String[] args) {
        String directory = ".";
        if (args.length > 0) {
            directory = args[0];
        }
        Tree tree = new Tree();
        tree.print(directory);
        (new Printer(directory, tree.buffer)).printFile();
    }
}

/**
 * Adapted from https://github.com/kddnewton/tree/blob/main/Tree.java
 */
class Tree {
    public final ArrayList<File> buffer = new ArrayList<>();

    public void print(String directory) {
        System.out.println(".");
        walk(new File(directory), "");
    }

    private void register(File file) {
        // add code files to buffer
        if (!file.isDirectory() && file.getName().endsWith(".java")) {
            buffer.add(file);
        }
    }

    private void walk(File folder, String prefix) {
        File file;
        File[] fileList = folder.listFiles();
        Arrays.sort(fileList);

        for (int index = 0; index < fileList.length; index++) {
            file = fileList[index];
            register(file);

            if (index == fileList.length - 1) {
                System.out.println(prefix + "└── " + file.getName());
                if (file.isDirectory()) {
                    walk(file, prefix + "    ");
                }
            } else {
                System.out.println(prefix + "├── " + file.getName());
                if (file.isDirectory()) {
                    walk(file, prefix + "│   ");
                }
            }
        }
    }
}

class Printer {
    private final String rootDir;
    private final ArrayList<File> files;

    public Printer(String directory, ArrayList<File> fileList) {
        rootDir = directory;
        files = new ArrayList<>(fileList);
    }

    public void printFile() {
        // open a file to write to
        File output = new File("jls.csv");
        try (PrintWriter writer = new PrintWriter(output)) {
            files.stream()
                // convert each file in list to some entry [relPath, pkg, classname]
                .map(this::extractInfo)
                // for each entry, print line to file
                .forEach(writer::println);
        } catch (FileNotFoundException ignored) {
            // ignore
        }
    }

    private String extractInfo(File file) {
        String relPath =
            "." + FileSystems.getDefault().getSeparator() + Path.of(rootDir).relativize(file.toPath());
        String packageName = "unnamed package";
        String classname = file.getName().substring(0, file.getName().length() - 5);
        // open the file and find its package name
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().strip();
                if (line.matches("^package \\w+[.\\w+]*;")) {
                    packageName = line.substring(8, line.length() - 1);
                    break;
                }
            }
        } catch (FileNotFoundException ignored) {
            // don't care
        }
        return String.join(",", relPath, packageName, classname);
    }
}
