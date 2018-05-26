package com.polimi.childcare.client.ui.controllers;

import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public abstract class UndecoratedDraggableStageController  extends BaseStageController
{
    UndecoratedDraggableStageController(URL fxmlPath) throws IOException
    {
        super(fxmlPath);
        initStyle(StageStyle.UNDECORATED);
    }

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
    protected abstract Pane getToolbarButtonsPane();
    protected abstract Node getRootNode();
    protected abstract Label getTitleLabel();

    //Variabili evento di drag/posizionamento/ridimensionamento nella finestra in caso di maximized
    private double xOffset;
    private double yOffset;

    //Variabili Maximize
    private double minimizedWidth;
    private double minimizedHeight;

    //Toolbar Buttons
    private byte toolBarButtonVisibilityMask = (byte)0xff;

    //Resize
    private int resizeDirectionMask = 0xffff; //Di default abilitata in tutte le direzioni

    //Toolbar Buttons
    public void setToolbarButtonsVisibilityMask(byte newMask)
    {
        this.toolBarButtonVisibilityMask = newMask;
        UpdateButtonVisibility();
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
                    if (!isMaximized()) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                }
            });

            getWindowDragParent().setOnMouseDragged((event) -> {

                if(event.isPrimaryButtonDown())
                {
                    if(!isMaximized())
                    {
                        setX(event.getScreenX() - xOffset);
                        setY(event.getScreenY() - yOffset);
                    }
                    else
                    {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                        this.setUndecoratedMaximized(false);
                    }
                }
            });
        }

        if(getCloseButton() != null)
            getCloseButton().setOnMouseClicked(mouseEvent ->
                    close());

        if(getMaximizeButton() != null)
            getMaximizeButton().setOnMouseClicked(mouseEvent ->
                    this.setUndecoratedMaximized(!this.isMaximized()));

        if(getMinimizeButton() != null)
            getMinimizeButton().setOnMouseClicked(mouseEvent ->
                    setIconified(true));


        //Gestisce gli eventi di resize
        //if(getRootNode() != null)
        //    getRootNode().setOnMouseDragged(this::OnResizeEvent);

        UpdateButtonVisibility();
    }

    /**
     * Permette l'utilizzo di Label per impostare il titolo (dato che lo stage non è decorato)
     * @param title Titolo da impostare
     */
    @Override
    public void requestSetTitle(String title)
    {
        if(getTitleLabel() != null)
            getTitleLabel().setText(title);

        super.requestSetTitle(title);
    }

    public final void setUndecoratedMaximized(boolean maximized)
    {
        //Se devo mettere la finestra a full-screen, salvo la posizione attuale della finestra per quando toglierò il fullscreen
        if(!this.isMaximized() && maximized)
        {
            xOffset = this.getX();
            yOffset = this.getY();
            minimizedWidth = this.getWidth();
            minimizedHeight = this.getHeight();

            Rectangle2D screenBounds = Screen.getScreensForRectangle(xOffset, yOffset, this.getWidth(), this.getHeight()).get(0).getVisualBounds();
            //Sposto lo stage in alto a sinistra
            this.setX(screenBounds.getMinX());
            this.setY(screenBounds.getMinY());

            this.setWidth(screenBounds.getWidth());
            this.setHeight(screenBounds.getHeight());
        }
        else if(this.isMaximized() && !maximized)
        {
            //Ripristino la finestra
            this.setX(xOffset);
            this.setY(yOffset);
            this.setWidth(minimizedWidth);
            this.setHeight(minimizedHeight);
        }

        this.setMaximized(maximized);
    }

    public final void setResizeDirection(int direction)
    {
        this.resizeDirectionMask = direction;
    }

    public final boolean canResizeInDirection(EResizeDirection direction)
    {
        return (this.resizeDirectionMask & direction.getDirection()) != 0x0000;
    }

    private void OnResizeEvent(MouseEvent mouseEvent)
    {
        if (mouseEvent.isPrimaryButtonDown())
        {
            double width = getWidth();
            double height = getHeight();

            // Horizontal resize.
            if (canResizeInDirection(EResizeDirection.Left)) {
                if ((width > getMinWidth()) || (mouseEvent.getX() < 0)) {
                    setWidth(width - mouseEvent.getScreenX() + getX());
                    setX(mouseEvent.getScreenX());
                }
            } else if (canResizeInDirection(EResizeDirection.Right)
                    && ((width > getMinWidth()) || (mouseEvent.getX() > 0))) {
                setWidth(width + mouseEvent.getX());
            }

            // Vertical resize.
            if (canResizeInDirection(EResizeDirection.Top)) {
                if ((height > getMinHeight()) || (mouseEvent.getY() < 0)) {
                    setHeight(height - mouseEvent.getScreenY() + getY());
                    setY(mouseEvent.getScreenY());
                }
            } else if (canResizeInDirection(EResizeDirection.Bottom)) {
                if ((height > getMinHeight()) || (mouseEvent.getY() > 0)) {
                    setHeight(height + mouseEvent.getY());
                }
            }
        }
    }

    //Toolbar Buttons


    public boolean isButtonVisible(byte button)
    {
        return (this.toolBarButtonVisibilityMask & button) != 0;
    }

    private void UpdateButtonVisibility()
    {
        if(getToolbarButtonsPane() != null)
        {
            getToolbarButtonsPane().getChildren().clear();

            if(isButtonVisible(ToolbarButtons.Minimize))
                getToolbarButtonsPane().getChildren().add(getMinimizeButton());

            if(isButtonVisible(ToolbarButtons.Maximize))
                getToolbarButtonsPane().getChildren().add(getMaximizeButton());

            if(isButtonVisible(ToolbarButtons.Close))
                getToolbarButtonsPane().getChildren().add(getCloseButton());
        }
    }

}
