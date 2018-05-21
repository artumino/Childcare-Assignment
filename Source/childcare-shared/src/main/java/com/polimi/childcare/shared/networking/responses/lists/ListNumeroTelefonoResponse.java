package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.NumeroTelefono;

import java.util.List;

public class ListNumeroTelefonoResponse extends ListResponse<NumeroTelefono>
{
    public ListNumeroTelefonoResponse(int code, List<NumeroTelefono> payload) {
        super(code, payload);
    }
}
