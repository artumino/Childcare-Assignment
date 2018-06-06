package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperationVault;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;

public abstract class NetworkedSubScene implements ISubSceneController
{
    // Le operazioni contenute nel vault di rete sono uniche per tipo e vengono cancellate al detach delle scene
    protected NetworkOperationVault networkOperationVault;

    public NetworkedSubScene()
    {
        networkOperationVault = new NetworkOperationVault();
    }

    @Override
    public void detached() {

        //In caso di detach, sospendi tutte le richieste nel vault
        networkOperationVault.abortAllOperations();
    }
}
