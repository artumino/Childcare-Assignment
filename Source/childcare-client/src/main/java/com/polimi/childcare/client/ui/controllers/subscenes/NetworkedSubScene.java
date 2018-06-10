package com.polimi.childcare.client.ui.controllers.subscenes;

import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.shared.networking.NetworkOperationVault;
import com.polimi.childcare.client.ui.controllers.BaseStageController;
import com.polimi.childcare.client.ui.controllers.ChildcareBaseStageController;
import com.polimi.childcare.client.ui.controllers.ISubSceneController;
import com.polimi.childcare.client.ui.controllers.stages.networking.BlockingNetworkOperationStageController;
import javafx.stage.Modality;

import java.io.IOException;

/**
 * Scene contenente una NetworkOperationVault per operazioni in rete strettamente legate alla scena attuale
 * e metodi per bloccare l'interfaccia durante l'esecuzione di operazioni critiche
 */
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

    /**
     * Metodo che apre una finestra bloccante per l'applicazione il cui unico scopo è eseguire un'operazione in rete
     * @param networkOperation Operazione da eseguire
     * @param callback Callback una volta eseguita l'operazione (e chiusa la finestra bloccante)
     */
    protected void ShowBlockingNetworkOperationStage(NetworkOperation networkOperation, BaseStageController.OnStageClosingCallback callback)
    {
        try {
            ChildcareBaseStageController blockingOperationController = new ChildcareBaseStageController();
            blockingOperationController.setContentScene(getClass().getClassLoader().getResource(BlockingNetworkOperationStageController.FXML_PATH), networkOperation);
            blockingOperationController.initOwner(getRoot().getScene().getWindow());
            blockingOperationController.initModality(Modality.APPLICATION_MODAL); //Blocco tutto
            blockingOperationController.setOnClosingCallback(callback);
            blockingOperationController.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
