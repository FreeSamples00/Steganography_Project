import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DecodeFrame extends JFrame {
    private final ImageProcessor decoder = new ImageProcessor();
    private boolean imageLoaded = false;
    private boolean keyLoaded = false;
    private boolean outputLoaded = false;

    public DecodeFrame() {
        //sets up JFrame elements
        setSize(750, 200);
        setTitle("Decoder");
        setLayout(new GridLayout());
        JButton loadImageButton = new JButton("Load Image To Decode");
        JButton loadKeyImageButton = new JButton("Load Key Image");
        JButton loadOutputFolderButton = new JButton("Choose Output Location");
        JButton decodeButton = new JButton("Decode");
        //mapping button actions
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = FileIO.getImage("Select an Image", null);
                    if (file != null) {
                        decoder.setInputFile(file);
                        imageLoaded = true;
                        loadImageButton.setBackground(Color.green);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file.");
                }
            }
        });
        loadKeyImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = FileIO.getImage("Select a key image", null);
                    if (file != null) {
                        decoder.setKeyImage(file);
                        keyLoaded = true;
                        loadKeyImageButton.setBackground(Color.green);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file");
                }
            }
        });
        loadOutputFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File folder = FileIO.getFolder("Choose an output Folder", null);
                if (folder != null) {
                    String fileName = "";
                    do {
                        fileName = JOptionPane.showInputDialog(null, "Enter output filename");
                    } while (fileName == null || fileName.isEmpty());
                    decoder.setOutputFile(folder, fileName, ".txt");
                    outputLoaded = true;
                    loadOutputFolderButton.setBackground(Color.green);
                }
            }
        });
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (imageLoaded && keyLoaded && outputLoaded) {
                    try {
                        decoder.decode();
                        int choice = JOptionPane.showOptionDialog(null, "Text encoded to " + decoder.getOutputFile().getAbsolutePath() + "\nOpen file location?",
                                "Output", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, "Yes");
                        if (choice == JOptionPane.YES_OPTION) {
                            FileIO.openFileLocation(decoder.getOutputFile());
                        }
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.toString());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Not all files have been loaded");
                }
            }
        });
        add(loadImageButton);
        add(loadKeyImageButton);
        add(loadOutputFolderButton);
        add(decodeButton);
        setVisible(true);
    }
}
