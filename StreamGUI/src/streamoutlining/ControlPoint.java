package streamoutlining;

/**
 *
 * @author Kyle Lusk
 */

import java.awt.Point;

public class ControlPoint 
{
    private int name;
    private Point location;
    
    public ControlPoint(int i, Point p)
    {
        i = name;
        p = location;
    }
    
    public int getName()
    {
        return name;
    }
    
    public Point getLocation()
    {
        return location;
    }
    
    public void setName(int i)
    {
        i = name;
    }
    
    public void setLocation(Point p)
    {
        p = location;
    }
}
