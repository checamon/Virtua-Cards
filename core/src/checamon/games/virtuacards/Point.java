package checamon.games.virtuacards;

import java.util.Iterator;
import java.util.List;

/**
 * Created by angelcheca on 16/11/15.
 */
public class Point {
    private float x;
    private float y;

    public Point (float xNew, float yNew)
    {
        x = xNew;
        y = yNew;
    }

    public float getX ()
    {
        return x;
    }

    public float getY ()
    {
        return y;
    }

    public boolean insideSquare(int deltaX, int deltaY)
    {
        boolean result = false;
        if (x <= deltaX && y <= deltaY)
            result = true;

        return result;
    }

    public boolean insideRightSquare(float deltaX, float deltaY, float originalY)
    {
        boolean result = false;
        if (x <= x + deltaX && y <= originalY + (deltaY/2) && y > originalY - (deltaY/2))
            result = true;

        return result;
    }

    public boolean insideLeftSquare(float deltaX, float deltaY, float originalY)
    {
        boolean result = false;
        if (x >= x - deltaX && y <= originalY + (deltaY/2) && y > originalY - (deltaY/2))
            result = true;

        return result;
    }

    public static boolean pointListInsideDoubleTouchedDrag(List<Point> points, float deltaX, float deltaY){
        boolean result = true;
        Point p0 = null;
        Point p1 = null;
        int direction = 0;
        for (Iterator<Point> it = points.iterator(); it.hasNext();){
            Point p = it.next();

            if (p1 == null)
                p1 = p;

            if (p0 != null) {
                if (p.getX() >= p0.getX()) {
                    if (direction == 0)
                        direction++; // Always 1 when going to the right

                    if (!p.insideRightSquare(deltaX,deltaY, points.get(0).getY()) || direction > 1) //Already changed direction to the left and now has come back to the right or outside the range
                    {
                        result = false;
                        break;
                    }
                } else
                {
                    if (direction == 1)
                        direction++; // Always 2 when going to the left

                    if (!p.insideLeftSquare(deltaX, deltaY, points.get(0).getY()) || direction == 0 || direction > 2) //Already changed direction several times or has started going left or outside range
                    {
                        result = false;
                        break;
                    }
                }
            }
            p0 = p;
        }
        if (direction < 2) // less than two changes of direction
            result = false;

        return result;
    }

    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
