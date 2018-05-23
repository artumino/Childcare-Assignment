package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.PianoViaggi;

import java.util.List;

public class ListPianoViaggiResponse extends ListResponse<PianoViaggi>
{
    public ListPianoViaggiResponse(int code, List<PianoViaggi> payload) {
        super(code, payload);
    }
}
