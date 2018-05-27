package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.ui.OrderedFilteredList;
import com.polimi.childcare.client.ui.components.FilterComponent;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.shared.entities.Menu;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class MensaSubsceneController implements ISubSceneController
{
    //TODO: Implementare
    public static final String FXML_PATH = "";

    @FXML private TableView<Menu> tableMenu;
    @FXML private Button btnRefresh;
    @FXML private TextField txtFilterMenu;
    @FXML private TextField txtFilterPasti;
    @FXML private Button btnAddMenu;
    @FXML private Button btnAddPasto;


    //Generated
    private FilterComponent<Menu> filterMenu;
    private OrderedFilteredList<Menu> listMenu;

    @FXML
    protected void initialize()
    {

    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {

    }

    @Override
    public void detached()
    {

    }

    void OnMenuResponseRecived(BaseResponse response)
    {

    }

    void OnPastiResponseRecived(BaseResponse response)
    {

    }

    private void redrawData()
    {

    }

    @Override
    public Region getSceneRegion()
    {
        return null;
    }

    @Override
    public Parent getRoot()
    {
        return null;
    }

    @Override
    public Scene setupScene(Parent parent)
    {
        return null;
    }
}
