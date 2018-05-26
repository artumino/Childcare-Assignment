package com.polimi.childcare.client.ui.controllers.subscenes.submenus.anagrafica;

import com.polimi.childcare.client.ui.controllers.ISceneController;
import com.polimi.childcare.shared.entities.Contatto;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Collection;

public class ContattiSubmenuController extends AnagraficaSubmenuBase<Contatto>
{
    @Override
    protected Collection<TableColumn<Contatto, ?>> setupColumns()
    {
        return null;
    }

    @Override
    protected void setupFilterNodes()
    {

    }

    @Override
    protected void setupControlNodes()
    {

    }

    @Override
    protected Collection<Node> getShownFilterElements()
    {
        return null;
    }

    @Override
    protected Collection<Node> getShownControlElements()
    {
        return null;
    }

    @Override
    public void attached(ISceneController sceneController, Object... args)
    {

    }

    @Override
    public void detached()
    {

    }
}
