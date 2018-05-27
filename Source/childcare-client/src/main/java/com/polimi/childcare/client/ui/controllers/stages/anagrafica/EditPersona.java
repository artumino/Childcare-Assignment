package com.polimi.childcare.client.ui.controllers.stages.anagrafica;

import com.polimi.childcare.client.ui.constants.ToolbarButtons;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controls.DragAndDropTableView;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.entities.Persona;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class EditPersona implements ISubSceneController
{
    @FXML private DragAndDropTableView<Genitore> tableGenitori;
    private Persona linkedPersona;
    private ChildcareBaseStageController stageController;
    private Parent root;

    @FXML private AnchorPane rootPane;



    @Override
    public void attached(ISceneController sceneController, Object... args)
    {
        if(sceneController instanceof ChildcareBaseStageController)
            this.stageController = (ChildcareBaseStageController)sceneController;


        if(this.stageController != null)
        {
            this.stageController.setToolbarButtonsVisibilityMask((byte)(ToolbarButtons.Close | ToolbarButtons.Minimize));

            if(args != null && args.length > 0 && args[0] instanceof Persona)
                this.linkedPersona = (Persona)args[0];

            this.stageController.requestSetTitle("Aggiorna - " + linkedPersona.getNome() + " " + linkedPersona.getCognome());

            TableColumn<Genitore, String> cName = new TableColumn<>("Nome");
            cName.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getNome()));

            tableGenitori.getColumns().addAll(cName);
            tableGenitori.dragForClass(Genitore.class);
        }
    }

    @Override
    public void detached()
    {

    }


    @Override
    public Region getSceneRegion() {
        return this.rootPane;
    }

    @Override
    public Parent getRoot()
    {
        return this.root;
    }

    @Override
    public Scene setupScene(Parent parent)
    {
        this.root = parent;
        return root.getScene();
    }
}
