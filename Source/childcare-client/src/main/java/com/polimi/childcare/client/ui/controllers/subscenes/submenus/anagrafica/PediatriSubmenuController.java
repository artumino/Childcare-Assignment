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

import java.util.Collection;

public class PediatriSubmenuController extends ContattiSubmenuController
{
    @Override
    protected void refreshData()
    {
        if(this.pendingOperation != null)
            ClientNetworkManager.getInstance().abortOperation(this.pendingOperation);

        this.pendingOperation = new NetworkOperation(
                new FilteredPediatraRequest(0, 0, false, null, null),
                this::OnContattiResponseRecived,
                true);

        //Provo ad aggiornare i dati
        ClientNetworkManager.getInstance().submitOperation(this.pendingOperation);
    }
}
