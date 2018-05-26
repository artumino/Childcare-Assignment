package com.polimi.childcare.client.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class BaseStageController extends Stage implements ISceneController
{
    //Class constant properties
    
    public double getControllerWidth() { return 100; }
    public double getControllerHeight() { return 100; }

    //Return Callback
    private OnStageClosingCallback onClosingCallback;

    BaseStageController(URL fxmlPath) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlPath);
        loader.setController(this);
        Parent parent = loader.load(fxmlPath.openStream());
        setScene(setupScene(parent));
    }

    @Override
    public Scene setupScene(Parent parent) {
        return new Scene(parent, this.getControllerWidth(), this.getControllerHeight());
    }

    public void requestClose(Object... returnArgs)
    {
        this.close(returnArgs);
    }

    public void setOnClosingCallback(OnStageClosingCallback onClosingCallback)
    {
        this.onClosingCallback = onClosingCallback;
    }

    public final void close(Object... data)
    {
        if(this.onClosingCallback != null)
            this.onClosingCallback.onResult(data);

        super.close();
    }

    public void requestSetTitle(String title)
    {
        setTitle(title);
    }

    public interface OnStageClosingCallback
    {
        void onResult(Object... args);
    }
}
