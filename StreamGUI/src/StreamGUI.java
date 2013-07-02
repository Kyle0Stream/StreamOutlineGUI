
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StreamGUI extends JFrame
{
    private FileWriter log;
    private BufferedWriter out;
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
    
    private JLabel sideLengthLabel;
    private JTextField sideLengthText;
    
    private JLabel hfovLabel;
    private JTextField hfovText;
    
    private JButton done;
    
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
        ActionListener fileName = new browseListener();
        browse.addActionListener(fileName);
        
        //CREATES TEXT FIELD AND LABEL FOR CORNER A-D
        cornerALabel = new JLabel("Corner A (X,Y): ");
        cornerAText = new JTextField(5);
        
        cornerBLabel = new JLabel("Corner B (X,Y): ");
        cornerBText = new JTextField(5);
        
        cornerCLabel = new JLabel("Corner C (X,Y): ");
        cornerCText = new JTextField(5);
        
        cornerDLabel = new JLabel("Corner D (X,Y): ");
        cornerDText = new JTextField(5);
        
        sideLengthLabel = new JLabel("Square Side Length: ");
        sideLengthText = new JTextField(5);
        
        hfovLabel = new JLabel("HFOV: ");
        hfovText = new JTextField(5);

        done = new JButton("DONE");
        MouseListener listener = new MouseListener() 
        { 
            @Override
            public void mousePressed(MouseEvent event) 
            {
                try 
                {
                    out.write("CornerA (X,Y): " + cornerAText.getText());
                    out.newLine();
                    out.write("CornerB (X,Y): " + cornerBText.getText());
                    out.write("CornerC (X,Y): " + cornerCText.getText());
                    out.newLine();
                    out.write("CornerD (X,Y): " + cornerDText.getText());
                    out.newLine();
                    out.write("Square Side Length: " + sideLengthText.getText());
                    out.newLine();
                    out.write("HFOV: " + hfovText.getText());
                    out.newLine();
                    out.close();
                } catch (IOException ex) 
                {
                    Logger.getLogger(StreamGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            @Override
            public void mouseReleased(MouseEvent event) {}
            @Override
            public void mouseClicked(MouseEvent event) {}
            @Override
            public void mouseEntered(MouseEvent event) {}
            @Override
            public void mouseExited(MouseEvent event) {}
        };

        done.addMouseListener(listener);

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
        bottom.add(sideLengthLabel);
        bottom.add(sideLengthText);
        bottom.add(hfovLabel);
        bottom.add(hfovText);
        bottom.add(done);
        this.add(bottom);
    }

    public static void main(String[] args) throws IOException
    {
        JFrame frame = new StreamGUI();
        frame.setTitle("StreamGUI");
        frame.setSize(350,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        FileWriter log = new FileWriter("E:/log.txt");
        BufferedWriter out = new BufferedWriter(log);
    }
}
