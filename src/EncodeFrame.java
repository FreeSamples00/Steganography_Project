import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class EncodeFrame extends JFrame {
    private final ImageProcessor encoder = new ImageProcessor();
    private boolean imageLoaded = false;
    private boolean textLoaded = false;
    private boolean outputLoaded = false;
    public EncodeFrame() {
        setSize(750,200);
        setTitle("Encoder");
        setLayout(new GridLayout());
        JButton loadImageButton = new JButton("Load Image");
        JButton loadTextButton = new JButton("Load Text File");
        JButton loadOutputFolderButton = new JButton("Choose Output Location");
        JButton encodeButton = new JButton("Encode");
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = FileIO.getImage("Select an Image", null);
                    if (file != null) {
                        encoder.setInputFile(file);
                        imageLoaded = true;
                        loadImageButton.setBackground(Color.green);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file.");
                }
            }
        });
        loadTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String string = FileIO.getTXTFromFile("Select a Text File", null);
                    if (string != null) {
                        encoder.setInputText(string);
                        textLoaded = true;
                        loadTextButton.setBackground(Color.green);
                    }
            } catch (ImageProcessor.SizeException ex) {
                    JOptionPane.showMessageDialog(null, "Error Reading file.");
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
                    encoder.setOutputFile(folder, fileName, ".png");
                    outputLoaded = true;
                    loadOutputFolderButton.setBackground(Color.green);
                }
            }
        });
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (imageLoaded && textLoaded && outputLoaded) {
                    try {
                        encoder.encode();
                        int choice = JOptionPane.showOptionDialog(null, "Text encoded to " + encoder.getOutputFile().getAbsolutePath() + "\nOpen file location?",
                                "Output", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, "Yes");
                        if (choice == JOptionPane.YES_OPTION) {
                            FileIO.openFileLocation(encoder.getOutputFile());
                        }
                        dispose();
                    } catch (ImageProcessor.SizeException ex) {
                        JOptionPane.showMessageDialog(null, "Text length exceeds space in image.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.toString());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Not all files have been loaded.");
                }
            }
        });
        add(loadImageButton);
        add(loadTextButton);
        add(loadOutputFolderButton);
        add(encodeButton);
        setVisible(true);
    }
}
