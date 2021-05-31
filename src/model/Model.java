package model;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Model
{
    final ArrayList<Shape> Formes;
    final ArrayList<Boolean> Selection;

    public Model()
    {
        Formes = new ArrayList<>();
        Selection = new ArrayList<>();
    }

    /**
     * Create an Ellipse with center's coordinate (x,y) , radius (radiusx,radiusy) and color
     * @param x x coordinate of ellipse center
     * @param y y coordinate of ellipse center
     * @param radiusx x radius of ellipse
     * @param radiusy y radius of ellipse
     * @param color color of ellipse
     */
    public void drawEllipse(double x, double y, double radiusx, double radiusy, Color color)
    {
        Ellipse ellipse = new Ellipse(radiusx,radiusy);
        ellipse.setCenterX(x);
        ellipse.setCenterY(y);
        ellipse.setFill(color);
        ellipse.setStrokeWidth(1);
        ellipse.setStroke(Color.BLACK);
        Formes.add(ellipse);
        Selection.add(false);
    }

    /**
     * Create a Rectangle with upper left coordinate (x,y) , width, height and color
     * @param x x upper left coordinate
     * @param y y upper left coordinate
     * @param width width value
     * @param height height value
     * @param color color of rectangle
     */
    public void drawRectangle(double x, double y, double width, double height, Color color)
    {
        Rectangle rectangle = new Rectangle(x,y,width,height);
        rectangle.setFill(color);
        rectangle.setStrokeWidth(1);
        rectangle.setStroke(Color.BLACK);
        Formes.add(rectangle);
        Selection.add(false);
    }

    /**
     * Create a Line with origin coordinate (x1,y1) , end coordinate (x2,y2) and color
     * @param x1 x origin coordinate
     * @param y1 y origin coordinate
     * @param x2 x end coordinate
     * @param y2 y end coordinate
     * @param color color of line
     */
    public void drawLine(double x1, double y1, double x2, double y2, Color color)
    {
        Line line = new Line(x1,y1,x2,y2);
        line.setFill(color);
        line.setStrokeWidth(15);
        line.setStroke(color);
        Formes.add(line);
        Selection.add(false);
    }

    /**
     * Set the given color to the given shape
     * @param s shape
     * @param c color
     */
    public void setColor(Shape s, Color c)
    {
        int index = Formes.indexOf(s);
        s.setFill(c);
        Formes.set(index, s);
        Selection.set(index, false);
    }

    /**
     * Select a given shape and give the user a visual feedback that the shape is selected
     * @param s shape
     */
    public void Selected(Shape s)
    {
        for (Shape other : Formes)
        {
            notSelected(other);
        }

        int index = Formes.indexOf(s);
        if(s instanceof Line)
        {
            s.setStroke(Color.YELLOW);
            s.setStrokeWidth(25);
            Formes.set(index,s);
            Selection.set(index, true);
        }
        else
        {
            s.setStroke(Color.YELLOW);
            s.setStrokeWidth(5);
            Formes.set(index,s);
            Selection.set(index, true);
        }
    }

    /**
     * Unselect a given shape and give the user a visual feedback that the shape is no longer selected
     * @param s shape
     */
    public void notSelected(Shape s)
    {
        int index = Formes.indexOf(s);
        if(s instanceof Line)
        {
            Paint color = s.getFill();
            s.setStrokeWidth(15);
            s.setStroke(color);
        }
        else{
            s.setStrokeWidth(1);
            s.setStroke(Color.BLACK);
        }
        Formes.set(index, s);
        Selection.set(index, false);
    }

    /**
     * Method to drag a given shape to the given coordinate
     * @param s shape
     * @param mousex mouse X coordinate to drag the shape at this position
     * @param mousey mouse Y coordinate to drag the shape at this position
     */
    public void Drag(Shape s, double mousex, double mousey)
    {
        int index = Formes.indexOf(s);

        if(s instanceof Rectangle)
        {
            double Height = ((Rectangle) s).getHeight();
            double Width = ((Rectangle) s).getWidth();
            double newx = (mousex) - Width/2.0;
            double newy = (mousey) - Height/2.0;
            ((Rectangle) s).setX(newx);
            ((Rectangle) s).setY(newy);
        }

        if(s instanceof Ellipse)
        {
            ((Ellipse) s).setCenterX(mousex);
            ((Ellipse) s).setCenterY(mousey);
        }

        if(s instanceof Line)
        {
            double x1 = ((Line) s).getStartX();
            double y1 = ((Line) s).getStartY();
            double x2 = ((Line) s).getEndX();
            double y2 = ((Line) s).getEndY();
            double distancex = Math.abs(x2-x1);
            double distancey = Math.abs(y2-y1);

            if(x2 < x1 && y2 < y1)
            {
                ((Line) s).setStartX(mousex);
                ((Line) s).setStartY(mousey);
                ((Line) s).setEndX(mousex-distancex);
                ((Line) s).setEndY(mousey-distancey);
            }

           else if(x2 < x1)
            {
                ((Line) s).setStartX(mousex);
                ((Line) s).setStartY(mousey);
                ((Line) s).setEndX(mousex-distancex);
                ((Line) s).setEndY(mousey+distancey);

            }

            else if(y2 < y1)
            {
                ((Line) s).setStartX(mousex);
                ((Line) s).setStartY(mousey);
                ((Line) s).setEndX(mousex+distancex);
                ((Line) s).setEndY(mousey-distancey);
            }
            else{
                ((Line) s).setStartX(mousex);
                ((Line) s).setStartY(mousey);
                ((Line) s).setEndX(mousex+distancex);
                ((Line) s).setEndY(mousey+distancey);
            }
        }

        Formes.set(index, s);
        Selection.set(index, true);
    }

    /**
     * Delete a given shape
     * @param s shape
     */
    public void delete(Shape s)
    {
        int index = Formes.indexOf(s);
        Formes.remove(s);
        Selection.remove(index);
    }

    /**
     * Clone a given shape
     * @param s shape
     */
    public void clone(Shape s)
    {

        if(s instanceof Rectangle)
        {
            double xclone = ((Rectangle) s).getX()-30;
            double yclone = ((Rectangle) s).getY()-30;
            double height = ((Rectangle) s).getHeight();
            double width = ((Rectangle) s).getWidth();
            drawRectangle(xclone, yclone, width, height, (Color) s.getFill());
        }

        if(s instanceof Ellipse)
        {
            double xclone = ((Ellipse) s).getCenterX()-30;
            double yclone = ((Ellipse) s).getCenterY()-30;
            double radiusx = ((Ellipse) s).getRadiusX();
            double radiusy = ((Ellipse) s).getRadiusY();
            drawEllipse(xclone,yclone,radiusx, radiusy,  (Color) s.getFill());
        }

        if(s instanceof Line)
        {
            double xclone = ((Line) s).getStartX()-30;
            double yclone = ((Line) s).getStartY()-30;
            double x2clone = ((Line) s).getEndX()-30;
            double y2clone = ((Line) s).getEndY()-30;
            drawLine(xclone,yclone,x2clone,y2clone,(Color) s.getFill());

        }

    }

    /**
     * Methode to get the shapes list
     * @return shapes list
     */
    public ArrayList<Shape> getFormes()
    {
        return Formes;
    }

    /**
     * Methode to get the Selection list
     * @return selection list
     */
    public ArrayList<Boolean> getSelection()
    {
        return Selection;
    }
}
