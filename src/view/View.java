package view;

import java.util.ArrayList;
import java.util.Collections;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class View
{
    Controller controller;

    @FXML
    private Button BtnClone;

    @FXML
    private Button BtnDelete;

    @FXML
    private ColorPicker ColorPicker;

    @FXML
    private RadioButton BtnEllipse;

    @FXML
    private RadioButton BtnRectangle;

    @FXML
    private RadioButton BtnLine;

    @FXML
    private RadioButton BtnSelect;

    @FXML
    private AnchorPane dessin;


    public View()
    {

        controller = new Controller();

    }

    double XOrigineRectangle = 0.0;
    double YOrigineRectangle = 0.0;

    @FXML
    public void initialize()
    {
        //Disable clone and delete button
        BtnClone.setDisable(true);
        BtnDelete.setDisable(true);


        dessin.setOnMousePressed(event ->
        {
            //Mouse coordinate
            double mouseX = event.getX();
            double mouseY = event.getY();

            //We get our lists for Shapes and Shape's state
            ArrayList<Shape> Formes = controller.ObtenirFormes();
            ArrayList<Boolean> Selection = controller.ObtenirSelection();

            //Get the color selected in the ColorPicker
            Color color = ColorPicker.getValue();

            if (!BtnSelect.isSelected())
            {
                //Disable clone and delete button
                BtnClone.setDisable(true);
                BtnDelete.setDisable(true);

                //Draw an Ellipse if ellipse radio button selected
                if (BtnEllipse.isSelected())
                {
                    controller.addEllipse(mouseX,mouseY,25.0,25.0,color);
                }

                //Draw Rectangle if rectangle radio button selected
                if (BtnRectangle.isSelected())
                {
                    controller.addRectangle(mouseX,mouseY,25.0, 25.0, color);
                    XOrigineRectangle = mouseX;
                    YOrigineRectangle = mouseY;
                }

                //Draw a Line if line radio button selected
                if (BtnLine.isSelected())
                {
                    controller.addLine(mouseX,mouseY,mouseX+100,mouseY+100,color);
                }
            }

            if (BtnSelect.isSelected())
            {
                for (Shape other : Formes)
                {
                    //Unselect all shapes
                    controller.notSelected(other);
                    Collections.fill(Selection, Boolean.FALSE);
                }
                for (Shape s : Formes)
                {
                    //We save the shape index to change it state
                    int index = Formes.indexOf(s);
                    if (s.contains(mouseX,mouseY))
                    {
                        //Set the shape state as "Selected (true)"
                        Selection.set(index, true);

                        //Enable clone and delete button
                        BtnClone.setDisable(false);
                        BtnDelete.setDisable(false);

                        //Redefine color
                        controller.setColor(s, color);

                        //We give the user a feedback that the shape is selected
                        controller.Selected(s);

                        //Clone the selected shape on click
                        BtnClone.setOnAction(clone ->
                        {
                            controller.clone(s);
                            maj();
                        });

                        //Delete the selected shape on click
                        BtnDelete.setOnAction(delete ->
                        {
                            controller.delete(s);
                            maj();
                        });

                    }
                }
            }
            maj();

        });

        dessin.setOnMouseDragged(event ->
        {
            //Mouse coordinate
            double mouseX = event.getX();
            double mouseY = event.getY();

            //We get our lists for Shapes and Shape's state
            ArrayList<Shape> Formes = controller.ObtenirFormes();
            ArrayList<Boolean> Selection = controller.ObtenirSelection();

            //Get the color selected in the ColorPicker
            Color color = ColorPicker.getValue();

            if (!BtnSelect.isSelected())
            {
                //Disable clone and delete button
                BtnClone.setDisable(true);
                BtnDelete.setDisable(true);
                Shape s = Formes.get(Formes.size()-1);

                //Draw an Ellipse if ellipse radio button selected
                if (BtnEllipse.isSelected())
                {
                    if(s instanceof Ellipse)
                    {
                        double centerx = ((Ellipse) s ).getCenterX();
                        double centery = ((Ellipse) s ).getCenterY();
                        ((Ellipse) s ).setRadiusX(Math.abs(mouseX-centerx));
                        ((Ellipse) s ).setRadiusY(Math.abs(mouseY-centery));
                    }

                }

                //Draw Rectangle if rectangle radio button selected
                if (BtnRectangle.isSelected())
                {
                    if(s instanceof Rectangle)
                    {
                        double newW = mouseX-XOrigineRectangle;
                        double newH = mouseY-YOrigineRectangle;

                        if(newH < 0.0 && newW < 0.0)
                        {
                            ((Rectangle) s ).setX(mouseX);
                            ((Rectangle) s ).setY(mouseY);
                            ((Rectangle) s ).setWidth(XOrigineRectangle-mouseX);
                            ((Rectangle) s ).setHeight(YOrigineRectangle-mouseY);

                        }

                        else if(newH < 0.0)
                        {
                            ((Rectangle) s ).setY(mouseY);
                            ((Rectangle) s ).setHeight(YOrigineRectangle-mouseY);
                            ((Rectangle) s ).setWidth(newW);
                        }

                        else if(newW < 0.0)
                        {
                            ((Rectangle) s ).setX(mouseX);
                            ((Rectangle) s ).setWidth(XOrigineRectangle-mouseX);
                            ((Rectangle) s ).setHeight(newH);
                        }

                        else{
                            ((Rectangle) s ).setWidth(newW);
                            ((Rectangle) s ).setHeight(newH);
                        }
                    }
                }

                //Draw a Line if line radio button selected
                if (BtnLine.isSelected())
                {
                    if(s instanceof Line)
                    {
                        ((Line) s ).setEndX(mouseX);
                        ((Line) s ).setEndY(mouseY);
                    }
                }

            }

            if(BtnSelect.isSelected())
            {
                for (Shape s : Formes)
                {
                    //We save the shape index to change it state
                    int index = Formes.indexOf(s);
                    boolean etat = Selection.get(index);

                    //We check if the mouse is on the shape and if the shape is selected
                    if (s.contains(mouseX,mouseY) && etat)
                    {

                        //Redefine color
                        controller.setColor(s, color);

                        //We give the user a feedback that the shape is selected
                        controller.Selected(s);

                        //We move the shape
                        controller.Drag(s, mouseX, mouseY);
                    }
                }
            }
            maj();

        });

    }


    /**
     * Methode to refresh the display
     */
    public void maj()
    {
        ArrayList<Shape> Formes = controller.ObtenirFormes();
        dessin.getChildren().clear();
        dessin.getChildren().addAll(Formes);
    }

}

