
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
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StreamGUI extends JFrame
{
    private static FileWriter log;
    private static BufferedWriter out;
    private static BufferedImage picImage;
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 100;
    private static final int SIDE_LENGTH = 8;
    private static final int fieldWidth = 15;
    private static File myfile;
    
    private static JFrame frame;
    private static JPanel bottom;
    
    private static JLabel fileNameLabel;
    private static JTextField fileNameText;
    private static JButton browse;
   
    private static JLabel cornerAXLabel;
    private static JTextField cornerAXText;
    private static JLabel cornerAYLabel;
    private static JTextField cornerAYText;
    
    private static JLabel cornerBXLabel;
    private static JTextField cornerBXText;
    private static JLabel cornerBYLabel;
    private static JTextField cornerBYText;
    
    private static JLabel cornerCXLabel;
    private static JTextField cornerCXText;
    private static JLabel cornerCYLabel;
    private static JTextField cornerCYText;
    
    private static JLabel cornerDXLabel;
    private static JTextField cornerDXText;
    private static JLabel cornerDYLabel;
    private static JTextField cornerDYText;
    
    private static JLabel sideLengthLabel;
    private static JTextField sideLengthText;
    
    private static JLabel hfovLabel;
    private static JTextField hfovText;
    
    private static JButton done;
    
    public StreamGUI()
    {
        bottom = new JPanel();

        //CREATES TEXT FIELD AND LABEL for FILENAME 
        fileNameLabel = new JLabel("Filename: ");
        fileNameText = new JTextField(fieldWidth);
        
        //CREATES BROWSE BUTTON
        browse = new JButton("Browse");
        
        class browseListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    try {
                        picImage = ImageIO.read(chooser.getSelectedFile());
                        myfile = chooser.getSelectedFile();
                        fileNameText.setText(myfile.getName());
                    } catch (IOException ex) {
                        System.out.println("IO Error");
                    }
                }
            }
        }
        ActionListener fileName = new browseListener();
        browse.addActionListener(fileName);
        
        //CREATES TEXT FIELDS AND LABELS FOR CORNER A-D, SIDELENGTH, AND HFOV
        cornerAXLabel = new JLabel("Corner A (X): ");
        cornerAXText = new JTextField(5);
        
        cornerAYLabel = new JLabel("Corner A (Y): ");
        cornerAYText = new JTextField(5);
        
        cornerBXLabel = new JLabel("Corner B (X): ");
        cornerBXText = new JTextField(5);
        
        cornerBYLabel = new JLabel("Corner B (Y): ");
        cornerBYText = new JTextField(5);
        
        cornerCXLabel = new JLabel("Corner C (X): ");
        cornerCXText = new JTextField(5);
        
        cornerCYLabel = new JLabel("Corner C (Y): ");
        cornerCYText = new JTextField(5);
        
        cornerDXLabel = new JLabel("Corner D (X): ");
        cornerDXText = new JTextField(5);
        
        cornerDYLabel = new JLabel("Corner D (Y): ");
        cornerDYText = new JTextField(5);
        
        sideLengthLabel = new JLabel("Square Side Length: ");
        sideLengthText = new JTextField(5);
        
        hfovLabel = new JLabel("HFOV: ");
        hfovText = new JTextField(5);
        
        //CREATES DONE BUTTON
        done = new JButton("DONE");
        class doneListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                try {
                    onDoneClick();
                } catch (IOException ex) {
                    System.out.println("IO Error");
                }
            }
        }
        ActionListener doneButton = new doneListener();
        done.addActionListener(doneButton);

        //CREATES PANEL
        bottom.add(fileNameLabel);
        bottom.add(fileNameText);
        bottom.add(browse);
        bottom.add(cornerAXLabel);
        bottom.add(cornerAXText);
        bottom.add(cornerAYLabel);
        bottom.add(cornerAYText);
        bottom.add(cornerBXLabel);
        bottom.add(cornerBXText);
        bottom.add(cornerBYLabel);
        bottom.add(cornerBYText);
        bottom.add(cornerCXLabel);
        bottom.add(cornerCXText);
        bottom.add(cornerCYLabel);
        bottom.add(cornerCYText);
        bottom.add(cornerDXLabel);
        bottom.add(cornerDXText);
        bottom.add(cornerDYLabel);
        bottom.add(cornerDYText);
        bottom.add(sideLengthLabel);
        bottom.add(sideLengthText);
        bottom.add(hfovLabel);
        bottom.add(hfovText);
        bottom.add(done);
        this.add(bottom);
    }
    
    private void onDoneClick() throws IOException
    {
        out.write("Filename: " + fileNameText.getText());
        out.newLine();
        out.write("Image Height, Width: " + picImage.getHeight() + ", " 
                + picImage.getWidth());
        out.newLine();
        out.write("CornerA (X,Y): " + Double.parseDouble(cornerAXText.getText())
                + ", " + Double.parseDouble(cornerAYText.getText()));
        out.newLine();
        out.write("CornerB (X,Y): " + Double.parseDouble(cornerBXText.getText())
                + ", " + Double.parseDouble(cornerBYText.getText()));
        out.newLine();
        out.write("CornerC (X,Y): " + Double.parseDouble(cornerCXText.getText())
                + ", " + Double.parseDouble(cornerCYText.getText()));
        out.newLine();
        out.write("CornerD (X,Y): " + Double.parseDouble(cornerDXText.getText())
                + ", " + Double.parseDouble(cornerDYText.getText()));
        out.newLine();
        out.write("Square Side Length: " + Double.parseDouble(sideLengthText.getText()));
        out.newLine();
        out.write("HFOV: " + Double.parseDouble(hfovText.getText()));
        out.newLine();
        out.close();
        frame.dispose();
    }

    public static void main(String[] args) throws IOException
    {
        frame = new StreamGUI();
        frame.setTitle("StreamGUI");
        frame.setSize(350,230);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        log = new FileWriter("E:/log.txt");
        out = new BufferedWriter(log);
    }
}
