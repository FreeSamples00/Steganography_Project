import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileIO {
    private static final File userDir = new File(System.getProperty("user.home"));
    private static final CustomFileFilter ImageFilter = new CustomFileFilter("Images", ".jpg", ".png", ".bmp");
    private static final CustomFileFilter TextFilter = new CustomFileFilter("Text Files", ".txt", ".csv", ".tsv", ".log");
    private static void printErr(String message) {
        System.out.format("Input err: %s\n", message);
    }

    //uses JFileChooser to get an image file from the user
    public static File getImage(String title, JFrame frame) {
        JFileChooser chooser = new JFileChooser(userDir);
        chooser.setFileFilter(ImageFilter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle(title);
        int saveChoice = chooser.showOpenDialog(frame);
        if (saveChoice == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsoluteFile();
        } else {
            return null;
        }
    }
    //uses JFileCHooser to get a folder file from the user
    public static File getFolder(String title, JFrame frame) {
        JFileChooser chooser = new JFileChooser(userDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(title);
        int saveChoice = chooser.showOpenDialog(frame);
        if (saveChoice == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsoluteFile();
        } else {
            return null;
        }
    }
    //uses JFileChooser to get a plaintext file then reads and return its contents
    public static String getTXTFromFile(String title, JFrame frame) {
        JFileChooser chooser = new JFileChooser(userDir);
        chooser.setFileFilter(TextFilter);
        chooser.setDialogTitle(title);
        StringBuilder output = new StringBuilder();
        int saveChoice = chooser.showOpenDialog(frame);
        if (saveChoice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile().getAbsoluteFile();
            try {
                Scanner fileReader = new Scanner(file);
                while (fileReader.hasNextLine()) {
                    output.append(fileReader.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("File \"" + file.getAbsoluteFile() + "\" not found.");
                return getTXTFromFile(title, frame);
            }
            return output.toString();
        } else {
            return null;
        }
    }

    //open the specified file in the OS file explorer
    public static void openFileLocation(File file) {
        try {
            Desktop.getDesktop().open(file.getParentFile());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error opening file location.");
        }
    }

    //custom file filter that checks extensions of files as well as all files in nested folders
    public static class CustomFileFilter extends FileFilter {
        private String description;
        private String[] extensions;

        public CustomFileFilter(String description, String... extensions) {
            this.description = description;
            this.extensions = extensions;
        }

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                File[] nestedFiles  = file.listFiles();
                if (nestedFiles != null) {
                    for (File nestedFile : nestedFiles) {
                        if (nestedFile.isDirectory()) {
                            if (accept(nestedFile)) {
                                return true;
                            };
                        }
                        for (String extension : extensions) {
                            if (nestedFile.isFile() && nestedFile.getName().toLowerCase().endsWith(extension)) {
                                return true;
                        }}
                    }
                }
                return false;
            } else {
                for (String extension : extensions) {
                    if (file.getName().toLowerCase().endsWith(extension)) {
                        return true;
                    }
                }
                return false;
            }
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}
