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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import streamoutlining.ImageUtils;

public class StreamGUI extends JFrame
{
    private BufferedImage pointsImage;
    private BufferedImage outlineImage;
//    private BufferedImage resize;
    private final int SIDE_LENGTH = 8;
    private final int fieldWidth = 15;
    private int clickCount = 0;
    private int redPointsCount = 1;
    private int yellowPointsCount = 1;
    private int magentaOutlineCount = 1;
    
    private ArrayList<Point> yellowPoints;
    private ArrayList<Point> redPoints;
    private ArrayList<Point> magentaOutline;
    
    private File myfile;
    
    private JFileChooser chooser = new JFileChooser();
    
    private JFrame framePoints;
    private JFrame frameOutline;
    private JPanel bottom;
    
    private JLabel pictureLabel;
    
    private JLabel pointsLabel;
    private JTextField pointsText;
    private JButton pointsBrowse;
    
    private JLabel outlineLabel;
    private JTextField outlineText;
    private JButton outlineBrowse;
   
    private JLabel cornerAXLabel;
    private JTextField cornerAXText;
    private JLabel cornerAYLabel;
    private JTextField cornerAYText;
    
    private JLabel cornerBXLabel;
    private JTextField cornerBXText;
    private JLabel cornerBYLabel;
    private JTextField cornerBYText;
    
    private JLabel cornerCXLabel;
    private JTextField cornerCXText;
    private JLabel cornerCYLabel;
    private JTextField cornerCYText;
    
    private JLabel cornerDXLabel;
    private JTextField cornerDXText;
    private JLabel cornerDYLabel;
    private JTextField cornerDYText;
    
    private JLabel sideLengthLabel;
    private JTextField sideLengthText;
    
    private JLabel hfovLabel;
    private JTextField hfovText;
    
    private JButton done;
    
    public StreamGUI() throws IOException
    {
        bottom = new JPanel();

        //CREATES TEXT FIELD AND LABEL for POINTS BROWSE
        pointsLabel = new JLabel("Points: ");
        pointsText = new JTextField(fieldWidth);
        
        //CREATES BROWSE BUTTON
        pointsBrowse = new JButton("Browse");
        
        class pointsBrowseListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    try 
                    {
                        pointsImage = ImageIO.read(chooser.getSelectedFile());
                        myfile = chooser.getSelectedFile();
                        pointsText.setText(myfile.getName());
                        yellowPoints = ImageUtils.getPixelListForColor(pointsImage, Color.yellow);
                        redPoints = ImageUtils.getPixelListForColor(pointsImage, Color.red);
                        addSqPtsCtrlPts();
                    } catch (IOException ex) 
                    {
                        System.out.println("IO Error");
                    }
                }
            }
        }
        ActionListener fileName = new pointsBrowseListener();
        pointsBrowse.addActionListener(fileName);
        
        //CREATES TEXT FIELD AND LABEL for OUTLINE BROWSE 
        outlineLabel = new JLabel("Outline: ");
        outlineText = new JTextField(fieldWidth);
        
        //CREATES BROWSE BUTTON
        outlineBrowse = new JButton("Browse");
        
        class outlineBrowseListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    try 
                    {
                        outlineImage = ImageIO.read(chooser.getSelectedFile());
                        myfile = chooser.getSelectedFile();
                        outlineText.setText(myfile.getName());
                        magentaOutline = ImageUtils.getPixelListForColor(outlineImage, Color.magenta);
                        addOutline();
                    } catch (IOException ex) 
                    {
                        System.out.println("IO Error");
                    }
                }
            }
        }
        ActionListener fileName1 = new outlineBrowseListener();
        outlineBrowse.addActionListener(fileName1);
        
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
        String strSide = String.valueOf(SIDE_LENGTH);
        sideLengthText.setText(strSide);
        
        hfovLabel = new JLabel("HFOV: ");
        hfovText = new JTextField(5);
        
        //CREATES DONE BUTTON
        done = new JButton("DONE");
        class doneListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                try 
                {
                    onDoneClick();
                } catch (IOException ex) 
                {
                    System.out.println(ex);
                    ex.printStackTrace();
                }
            }
        }
        ActionListener doneButton = new doneListener();
        done.addActionListener(doneButton);

        //CREATES PANEL
        bottom.add(pointsLabel);
        bottom.add(pointsText);
        bottom.add(pointsBrowse);
        bottom.add(outlineLabel);
        bottom.add(outlineText);
        bottom.add(outlineBrowse);
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
    
    private void addOutline()
    {
        pictureLabel = new JLabel(new ImageIcon(outlineImage));
        frameOutline = new JFrame();
        frameOutline.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameOutline.getContentPane().add(pictureLabel);
        frameOutline.pack();
        JScrollPane scrollBar=new JScrollPane(pictureLabel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frameOutline.add(scrollBar);
        pictureLabel.setBackground(Color.black);
        pictureLabel.setOpaque(true);
        frameOutline.setTitle("Stream Outline");
        frameOutline.setLocation(350,0);
        frameOutline.setSize(900,700);
        frameOutline.setVisible(true);
    }
        
    private void addSqPtsCtrlPts()
    {
//        resize = resizeImage(pointsImage, BufferedImage.TYPE_INT_ARGB, 800,600);
//        pictureLabel = new JLabel(new ImageIcon(resize));
        pictureLabel = new JLabel(new ImageIcon(pointsImage));
        framePoints = new JFrame();
        MouseListener listener = new MouseListener() 
        { 
            @Override
            public void mousePressed(MouseEvent event) 
            {
                    if(clickCount==0)
                    {
                        double AX = (event.getX() + 0.5);
                        double AY = (event.getY() + 0.5);
                        String strAX = String.valueOf(AX);
                        String strAY = String.valueOf(AY);
                        cornerAXText.setText(strAX);
                        cornerAYText.setText(strAY);
                    }
                    if(clickCount==1)
                    {
                        double BX = (event.getX() + 0.5);
                        double BY = (event.getY() + 0.5);
                        String strBX = String.valueOf(BX);
                        String strBY = String.valueOf(BY);
                        cornerBXText.setText(strBX);
                        cornerBYText.setText(strBY);
                    }
                    if(clickCount==2)
                    {
                        double CX = (event.getX() + 0.5);
                        double CY = (event.getY() + 0.5);
                        String strCX = String.valueOf(CX);
                        String strCY = String.valueOf(CY);
                        cornerCXText.setText(strCX);
                        cornerCYText.setText(strCY);
                    }
                    if(clickCount==3)
                    {
                        double DX = (event.getX() + 0.5);
                        double DY = (event.getY() + 0.5);
                        String strDX = String.valueOf(DX);
                        String strDY = String.valueOf(DY);
                        cornerDXText.setText(strDX);
                        cornerDYText.setText(strDY);
                    }
                    clickCount++;
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
        pictureLabel.addMouseListener(listener);
        framePoints.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePoints.getContentPane().add(pictureLabel);
        framePoints.pack();
        pictureLabel.setBackground(Color.black);
        pictureLabel.setOpaque(true);
        JScrollPane scrollBar=new JScrollPane(pictureLabel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        framePoints.add(scrollBar);
        JOptionPane.showMessageDialog(framePoints, "Click on the four corners of the "
                + "same square to fill in points A-D.", "Information", JOptionPane.INFORMATION_MESSAGE);
        framePoints.setTitle("Square Points and Control Points");
        framePoints.setLocation(350,0);
        framePoints.setSize(900,700);
        framePoints.setVisible(true);
    }
    
    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int Width,int Height)
    {
       BufferedImage resizedImage = new BufferedImage(Width, Height, type);
       Graphics2D g = resizedImage.createGraphics();
       g.drawImage(originalImage, 0, 0, Width, Height, null);
       g.dispose();
       return resizedImage;
    }

    private void onDoneClick() throws IOException
    {
        BufferedWriter miscInfo = new BufferedWriter(new FileWriter("E:/miscInfo.txt"));
        BufferedWriter outline = new BufferedWriter(new FileWriter("E:/outline.txt"));
        BufferedWriter controlPoints = new BufferedWriter(new FileWriter("E:/controlPoints.txt"));
        BufferedWriter squareCorners = new BufferedWriter(new FileWriter("E:/squareCorners.txt"));
        PrintWriter pw = new PrintWriter(miscInfo);
        
        miscInfo.write("Points Filename: " + pointsText.getText());
        miscInfo.newLine();
        miscInfo.write("Image Height, Width: " + pointsImage.getHeight() + ", " 
                + pointsImage.getWidth());
        miscInfo.newLine();
        miscInfo.write("CornerA (X,Y): " + Double.parseDouble(cornerAXText.getText())
                + ", " + Double.parseDouble(cornerAYText.getText()));
        miscInfo.newLine();
        miscInfo.write("CornerB (X,Y): " + Double.parseDouble(cornerBXText.getText())
                + ", " + Double.parseDouble(cornerBYText.getText()));
        miscInfo.newLine();
        miscInfo.write("CornerC (X,Y): " + Double.parseDouble(cornerCXText.getText())
                + ", " + Double.parseDouble(cornerCYText.getText()));
        miscInfo.newLine();
        miscInfo.write("CornerD (X,Y): " + Double.parseDouble(cornerDXText.getText())
                + ", " + Double.parseDouble(cornerDYText.getText()));
        miscInfo.newLine();
        miscInfo.write("Square Side Length: " + Double.parseDouble(sideLengthText.getText()));
        miscInfo.newLine();
        miscInfo.write("HFOV: " + Double.parseDouble(hfovText.getText()));
        miscInfo.newLine();
        miscInfo.write("Outline Filename: " + outlineText.getText());
        miscInfo.newLine();
        miscInfo.newLine();
        miscInfo.write("Image Height, Width: " + outlineImage.getHeight() + ", " 
                + outlineImage.getWidth());
        miscInfo.newLine();
        
        //PIXEL LOCATIONS HERE (RED, YELLOW)
        for (Point p: redPoints)
        {
            controlPoints.write(p.x + ", " + p.y);
            controlPoints.newLine();
            redPointsCount++;
        }
        
        for (Point p: yellowPoints)
        {
            squareCorners.write(p.x + ", " + p.y);
            squareCorners.newLine();
            yellowPointsCount++;
        }

        //PIXEL LOCATIONS HERE (MAGENTA)
        for (Point p: magentaOutline)
        {
            outline.write(p.x + ", " + p.y);
            outline.newLine();
            magentaOutlineCount++;
        }
        miscInfo.close();
        controlPoints.close();
        squareCorners.close();
        outline.close();
        this.dispose();
        framePoints.dispose();
        frameOutline.dispose();
    }

    public static void main(String[] args) throws IOException
    {
        StreamGUI frameStreamGUI = new StreamGUI();
        frameStreamGUI.setTitle("StreamGUI");
        frameStreamGUI.setSize(350,260);
        frameStreamGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameStreamGUI.setVisible(true);
    }
}
