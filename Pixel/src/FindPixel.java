import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FindPixel
{
    public static int yellowCount = 0;
    public static int magentaCount = 0;
    public static int redCount = 0;
    public final static String IMG = "dscn0152nobackground.png";
    public static void main(String[] args) throws IOException 
    {
        //read image
        BufferedImage picImage = ImageIO.read(FindPixel.class.getResource(IMG));
        //write file
        FileWriter log = new FileWriter("E:/JAVA/StreamOutlineGUI/Pixel/src/log.txt");
        BufferedWriter out = new BufferedWriter(log);

        for(int y = 0; y < picImage.getHeight(); y++)
        {
            for(int x = 0; x < picImage.getWidth(); x++)
            {
                Color c = new Color(picImage.getRGB(x,y));
                
                if (c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 0)
                {
                    yellowCount++;
                    out.write(yellowCount + ") " + "Yellow Pixel found at = " + x + "," + y);
                    out.newLine();
                }
                if (c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 255)
                {
                    magentaCount++;
                    out.write(magentaCount + ") " + "Magenta Pixel found at = " + x + "," + y);
                    out.newLine();
                }
                if (c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 0)
                {
                    redCount++;
                    out.write(redCount + ") " + "Red Pixel found at = " + x + "," + y);
                    out.newLine();
                }
            }
        }
        out.write("Total Amount of Red pixels: " + redCount);
        out.newLine();
        out.write("Total Amount of Magenta pixels: " + magentaCount);
        out.newLine();
        out.write("Total Amount of Yellow pixels: " + yellowCount);
        out.close();
    }  
}
