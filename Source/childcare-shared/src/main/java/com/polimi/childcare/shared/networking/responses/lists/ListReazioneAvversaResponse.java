package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.ReazioneAvversa;

import java.util.List;

public class ListReazioneAvversaResponse extends ListResponse<ReazioneAvversa>
{
    public ListReazioneAvversaResponse(int code, List<ReazioneAvversa> payload) {
        super(code, payload);
    }
}
