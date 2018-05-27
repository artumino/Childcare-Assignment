package com.polimi.childcare.shared.networking.requests.special;

import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.MezzoDiTrasporto;
import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.util.HashMap;

public class GeneratePianiViaggioRequest extends BaseRequest
{
    private Gita gita;
    private HashMap<Gruppo, MezzoDiTrasporto> mappaGruppoToMezzoDiTrasporto;

    public GeneratePianiViaggioRequest(Gita gita, HashMap<Gruppo, MezzoDiTrasporto> mappaGruppoToMezzoDiTrasporto) {
        this.gita = gita;
        this.mappaGruppoToMezzoDiTrasporto = mappaGruppoToMezzoDiTrasporto;
    }

    public Gita getGita() {
        return gita;
    }

    public HashMap<Gruppo, MezzoDiTrasporto> getMappaGruppoToMezzoDiTrasporto() {
        return mappaGruppoToMezzoDiTrasporto;
    }
}
