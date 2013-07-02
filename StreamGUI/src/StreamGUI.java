
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
    
    private JPanel bottom;
    
    private JLabel fileNameLabel;
    private JTextField fileNameText;
    private JButton browse;
   
    private JLabel cornerALabel;
    private JTextField cornerAText;
    
    private JLabel cornerBLabel;
    private JTextField cornerBText;
    
    private JLabel cornerCLabel;
    private JTextField cornerCText;
    
    private JLabel cornerDLabel;
    private JTextField cornerDText;
    
    public StreamGUI()
    {
        bottom = new JPanel();
        //CREATES TEXT FIELD FOR FILENAME
        fileNameLabel = new JLabel("Filename: ");
        fileNameText = new JTextField(fieldWidth);
        
        //CREATES BUTTON
        browse = new JButton("Browse");
        
        class browseListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    File myfile = chooser.getSelectedFile();
                    fileNameText.setText(myfile.getName());
                }
            }
        }
        ActionListener listener = new browseListener();
        browse.addActionListener(listener);
        
        //CREATES TEXT FIELD AND LABEL FOR CORNER A-D
        cornerALabel = new JLabel("Corner A (X,Y): ");
        cornerAText = new JTextField(5);
        
        cornerBLabel = new JLabel("Corner B (X,Y): ");
        cornerBText = new JTextField(5);
        
        cornerCLabel = new JLabel("Corner C (X,Y): ");
        cornerCText = new JTextField(5);
        
        cornerDLabel = new JLabel("Corner D (X,Y): ");
        cornerDText = new JTextField(5);

        //CREATES PANEL
        bottom.add(fileNameLabel);
        bottom.add(fileNameText);
        bottom.add(browse);
        bottom.add(cornerALabel);
        bottom.add(cornerAText);
        bottom.add(cornerBLabel);
        bottom.add(cornerBText);
        bottom.add(cornerCLabel);
        bottom.add(cornerCText);
        bottom.add(cornerDLabel);
        bottom.add(cornerDText);
        this.add(bottom);
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
