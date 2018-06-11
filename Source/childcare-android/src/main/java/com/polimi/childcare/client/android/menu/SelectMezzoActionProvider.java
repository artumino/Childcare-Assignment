package com.polimi.childcare.client.android.menu;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import com.polimi.childcare.client.android.CacheManager;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.entities.PianoViaggi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectMezzoActionProvider extends ActionProvider
{
    private HashMap<MenuItem, MezzoDiTrasporto> mezziDiTrasportoMap;
    private MenuItem noneMenuItem;
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public SelectMezzoActionProvider(Context context)
    {
        super(context);
        mezziDiTrasportoMap = new HashMap<>();
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu)
    {
        super.onPrepareSubMenu(subMenu);

        mezziDiTrasportoMap.clear();
        subMenu.clear();

        if(CacheManager.getInstance(getContext()).getCurrentGita() != null &&
            CacheManager.getInstance(getContext()).getCurrentGita().getPianiViaggi() != null)
        {
            List<MezzoDiTrasporto> mezziDiTrasporto = new ArrayList<>();
            for(PianoViaggi pianoViaggi : CacheManager.getInstance(getContext()).getCurrentGita().getPianiViaggi())
            {
                if(pianoViaggi.getMezzo() != null &&
                        !mezziDiTrasporto.contains(pianoViaggi.getMezzo())) {
                    mezziDiTrasportoMap.put(subMenu.add(pianoViaggi.getMezzo().getTarga()), pianoViaggi.getMezzo());
                    mezziDiTrasporto.add(pianoViaggi.getMezzo());
                }
            }
        }

        noneMenuItem = subMenu.add("Nessuno");
    }

    public MezzoDiTrasporto getMezzoFromItem(MenuItem item)
    {
        return mezziDiTrasportoMap.get(item) != null ? mezziDiTrasportoMap.get(item) :
                (item == noneMenuItem ? new MezzoDiTrasporto(null, 0,0,0,null) : null);
    }

    @Override
    public View onCreateActionView()
    {
        return null;
    }
}
