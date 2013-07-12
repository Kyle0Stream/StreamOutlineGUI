/**
 *
 * @author Kyle
 */

package streamoutlining;

import java.awt.Point;

public class ImageMarker 
{
    public enum MarkerType { CONTROL_POINT, CORNER_POINT, OUTLINE_POINT };
    
    private MarkerType type;
    private String id;
    private Point location;
    
    public ImageMarker(MarkerType type, Point location )
    {
        this.type = type;
        this.location = location;
        this.id = null;
    }

    public Point getLocation()
    {
        return this.location;
    }
    
    public String getID()
    {
        return this.id;
    }

    public void setID(String id)
    {
        this.id = id;
    }
    
    public MarkerType getType()
    {
        return this.type;
    }
    
    @Override
    public String toString()
    {
        return type + " " + getID() + " at " + getLocation();
    }
    
}
