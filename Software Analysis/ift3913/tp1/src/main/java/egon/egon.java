package egon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class egon {
    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            throw new IOException("Not enough arguments");
        }
        String directory = args[0];
        //Reminder: threshold is in %
        int threshold = Integer.parseInt(args[1]);

        /**
         * Run jls over path given to obtain CSV file, then read CSV file
         */
        jls_egon.jls(directory);

        //CSV file should be in the same directory as egon our egon executable
        File csv = new File("jls.csv");

        //Scanner taken from lcsec
        ArrayList<String[]> files = new ArrayList<String[]>();
        Scanner scan = new Scanner(csv);
        scan.useDelimiter("\n");
        while (scan.hasNext()) {
            String nextLine = scan.next();
            String[] linesplit = Arrays.stream(nextLine.split(",")).map(String::strip).toArray(String[]::new);

            //Initialize nvloc and CSEC value to 0
            String[] fileline = new String[5];
            for(int i = 0; i<linesplit.length; i++) {
                fileline[i] = linesplit[i];
            }
            //nvloc value
            fileline[3] = "0";
            //csec value
            fileline[4] = "0";

            files.add(fileline);
        }
        scan.close();

        /**
         * Run nvloc over every class
         */
        for (int i = 0; i < files.size(); i++) {
            String[] currentfile = files.get(i);
            int nvlocresult = nvloc_egon.nvloc(directory+currentfile[0]);
            currentfile[3] = String.valueOf(nvlocresult);
            files.set(i, currentfile);
        }

        /**
         * Run lcsec over every class
         */
        int[] csecscores = lcsec_egon.lcsec(directory, files);
        for (int i = 0; i < files.size(); i++) {
            String[] currentfile = files.get(i);
            currentfile[4] = String.valueOf(csecscores[i]);
            files.set(i, currentfile);
        }

        /**
         * Compare to threshold (both NVLOC and CSEC have to be over threshold)
         */
        ArrayList<String[]> divinenvlocclasses = new ArrayList<String[]>();
        ArrayList<String[]> divinecsecclasses = new ArrayList<String[]>();
        ArrayList<String[]> divineclasses = new ArrayList<String[]>();

        //Sorted array by nvloc
        ArrayList<String[]> nvlocsort = new ArrayList<String[]>(files);
        Collections.sort(nvlocsort, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.parseInt(o1[3]) - (Integer.parseInt(o2[3]));
            }
        });

        //Sorted array by csec
        ArrayList<String[]> csecsort = new ArrayList<String[]>(files);
        Collections.sort(csecsort, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.parseInt(o1[4]) - (Integer.parseInt(o2[4]));
            }
        });

        for (int i = 0; i < nvlocsort.size(); i++) {
            String[] currentfile1 = nvlocsort.get(i);
            double nvlocpercentile = (Double.valueOf(i+1)/Double.valueOf(nvlocsort.size())*100);
            if (nvlocpercentile > 100-threshold) {
                divinenvlocclasses.add(currentfile1);
            }
        }
        for (int i = 0; i < csecsort.size(); i++) {
            String[] currentfile2 = csecsort.get(i);
            double csecpercentile = (Double.valueOf(i+1)/Double.valueOf(csecsort.size())*100);
            if (csecpercentile > 100-threshold) {
                divinecsecclasses.add(currentfile2);
            }
        }

        for (int i = 0; i < divinenvlocclasses.size(); i++) {
            String[] nvlocclass = divinenvlocclasses.get(i);
            for (int j = 0; j < divinecsecclasses.size(); j++) {
                String[] csecclass = divinecsecclasses.get(j);
                if (nvlocclass[0].equals(csecclass[0])) {
                    divineclasses.add(csecclass);
                }
            }
        }

        /**
         * Output result
         */
        ArrayList<File> divinefiles = new ArrayList<File>();
        for (String[] output : divineclasses) {
            divinefiles.add(new File(directory+output[0]));
            System.out.println(output[0] + ", " + output[1] + ", " + output[2] + ", " + output[3] + ", " + output[4]);
        }
        (new PrinterEgon(directory, divinefiles)).printFile();
    }
}

class PrinterEgon {
    private final String rootDir;
    private final ArrayList<File> files;

    public PrinterEgon(String directory, ArrayList<File> fileList) {
        rootDir = directory;
        files = new ArrayList<>(fileList);
    }

    public void printFile() {
        // open a file to write to
        File output = new File("egon.csv");
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
