import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    public Main() {
        //setting up the JFrame elements
        setTitle("Steganography");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 100);
        setLayout(new GridLayout());
        JButton encodeButton = new JButton("Encode");
        JButton decodeButton = new JButton("Decode");
        JButton quitButton = new JButton("Quit");
        quitButton.setBackground(Color.pink);
        //mapping button actions
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EncodeFrame();
            }
        });
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DecodeFrame();
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(encodeButton);
        add(decodeButton);
        add(quitButton);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
