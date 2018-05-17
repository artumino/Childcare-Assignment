package com.polimi.childcare.client.ui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class UndecoratedDraggableStageController  extends BaseStageController
{
    /**
     * Enum che fornisce la maschera per la direzione del resize
     */
    public enum EResizeDirection
    {
        Top(1), Bottom(2), Left(4), Right(8);

        int direction;
        EResizeDirection(int direction)
        {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }
    }

    //Elementi di interazione con lo stage
    protected abstract Node getWindowDragParent();
    protected abstract Node getMinimizeButton();
    protected abstract Node getMaximizeButton();
    protected abstract Node getCloseButton();
    protected abstract Node getRootNode();

    //Variabili evento di drag/posizionamento/ridimensionamento nella finestra in caso di maximized
    private double xOffset;
    private double yOffset;

    //Variabili Maximize
    private boolean isMaximized;
    private double minimizedWidth;
    private double minimizedHeight;

    //Resize
    private int resizeDirectionMask = 0xffff; //Di default abilitata in tutte le direzioni

    @Override
    public Scene setupStageAndShow(Stage stage, Parent parent)
    {
        stage.initStyle(StageStyle.UNDECORATED);
        return super.setupStageAndShow(stage, parent);
    }

    @FXML
    protected void initialize()
    {
        //Drag abilitato
        if(getWindowDragParent() != null)
        {
            getWindowDragParent().setOnMousePressed((event) -> {
                if(event.isPrimaryButtonDown())
                {
                    if (!isMaximized) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                }
            });

            getWindowDragParent().setOnMouseDragged((event) -> {

                if(event.isPrimaryButtonDown())
                {
                    if(!isMaximized)
                    {
                        this.linkedStage.setX(event.getScreenX() - xOffset);
                        this.linkedStage.setY(event.getScreenY() - yOffset);
                    }
                    else
                    {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                        this.setMaximized(false);
                    }
                }
            });
        }

        if(getCloseButton() != null)
            getCloseButton().setOnMouseClicked(mouseEvent ->
                    this.linkedStage.close());

        if(getMaximizeButton() != null)
            getMaximizeButton().setOnMouseClicked(mouseEvent ->
                    this.setMaximized(!this.isMaximized()));

        if(getMinimizeButton() != null)
            getMinimizeButton().setOnMouseClicked(mouseEvent ->
                    this.linkedStage.setIconified(true));


        //Gestisce gli eventi di resize
        //if(getRootNode() != null)
        //    getRootNode().setOnMouseDragged(this::OnResizeEvent);
    }

    public boolean isMaximized()
    {
        return this.isMaximized;
    }

    public void setMaximized(boolean maximized)
    {
        //Se devo mettere la finestra a full-screen, salvo la posizione attuale della finestra per quando toglierò il fullscreen
        if(!this.isMaximized() && maximized)
        {
            xOffset = this.linkedStage.getX();
            yOffset = this.linkedStage.getY();
            minimizedWidth = this.linkedStage.getWidth();
            minimizedHeight = this.linkedStage.getHeight();

            Rectangle2D screenBounds = Screen.getScreensForRectangle(xOffset, yOffset, this.linkedStage.getWidth(), this.linkedStage.getHeight()).get(0).getVisualBounds();
            //Sposto lo stage in alto a sinistra
            this.linkedStage.setX(screenBounds.getMinX());
            this.linkedStage.setY(screenBounds.getMinY());

            this.linkedStage.setWidth(screenBounds.getWidth());
            this.linkedStage.setHeight(screenBounds.getHeight());
        }
        else if(this.isMaximized() && !maximized)
        {
            //Ripristino la finestra
            this.linkedStage.setX(xOffset);
            this.linkedStage.setY(yOffset);
            this.linkedStage.setWidth(minimizedWidth);
            this.linkedStage.setHeight(minimizedHeight);
        }

        this.linkedStage.setMaximized(maximized);
        this.isMaximized = maximized;
    }
}
