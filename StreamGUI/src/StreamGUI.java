
/* Notes by Forrest (7/1/2013)
 * 
 *  Things to get from the image (e.g. DSCN001.JPG or DSCN001.PNG) itself:  
 *      - image width, image height
 *      - locations of the four corners of the square (in pixels)
 * 
 * Things to get from the user:
 *      - Which corners are which: label ABCD the red(?) corners of the square
 *      - Horizontal Field of View (HFOV) of the camera that took the pictures.
 *        (we think our red/silver cameras are either 67.5 or 69.5 or something,
 *         but we need our software to work for all cameras).
 * 
 *      - Real-world length of each side of the square (e.g. 8)
 * 
 * Then, either:
 *      A) create a text file (e.g. DSCN001.input.txt) with all the info.
 *         and run the MatLab program that will do the actual conversion
 *   or B) we should translate the MatLab program into Java, and actually do
 *        the math needed to "rectify" the perspective of the image
 * 
 * Eventually, we may want a program (probably a separate program?) for merging
 * multiple rectified images. We'll deal with that later.
 */

/**
 *
 * @author kl0601084
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StreamGUI extends JFrame
{
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 100;
    private static final int SIDE_LENGTH = 8;
    private static final int fieldWidth = 15;
    private JLabel fileNameLabel;
//    private JLabel cornerALabel;
    private JTextField fileNameText;
//    private JTextField cornerAText;
    private JButton button;
    private JLabel resultLabel;
    private JPanel panel;
    
    public StreamGUI()
    {
        resultLabel = new JLabel();
       // updateLabelDisplay();
       
        //CREATES TEXT FIELD FOR FILENAME
        fileNameLabel = new JLabel("Filename: ");
        fileNameText = new JTextField(fieldWidth);
        
        //CREATES TEXT FIELD FOR cornerA
//        cornerALabel = new JLabel("cornerA: ");
//        cornerAText = new JTextField(fieldWidth);
        
        //CREATES BUTTON
        button = new JButton("Browse");
        
        class buttonListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    File myfile = chooser.getSelectedFile();
                    fileNameLabel.setText(myfile.getName());
                }
            }
        }
        ActionListener listener = new buttonListener();
        button.addActionListener(listener);
        
        //CREATES PANEL
        panel = new JPanel();
        panel.add(fileNameLabel);
        panel.add(fileNameText);
//        panel.add(cornerALabel);
//        panel.add(cornerAText);
        panel.add(button);
        panel.add(resultLabel);
        this.add(panel);
    }

    public static void main(String[] args) 
    {
        JFrame frame = new StreamGUI();
        frame.setTitle("StreamGUI");
        frame.setSize(350,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
