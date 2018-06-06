package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredContattoOnlyRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredPediatraRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;

import java.util.Collection;

public class PediatriSubmenuController extends ContattiSubmenuController
{
    @Override
    protected void refreshData()
    {
        networkOperationVault.submitOperation(new NetworkOperation(
                new FilteredPediatraRequest(0, 0, false),
                this::OnContattiResponseRecived,
                true));
    }

    @Override
    protected void setupControlNodes() {
        super.setupControlNodes();

        if(btnAddContatto != null)
            btnAddContatto.setText("Aggiungi Pediatra");
    }

    @Override
    protected void OnNewContattoClicked(MouseEvent ignored) {
        ShowContattoDetails(new Pediatra());
    }
}
