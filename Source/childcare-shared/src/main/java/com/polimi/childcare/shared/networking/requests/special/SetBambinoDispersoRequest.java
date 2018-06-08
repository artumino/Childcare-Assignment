package com.polimi.childcare.shared.networking.requests.special;

public class SetBambinoDispersoRequest extends SetPresenzaRequest
{
    public SetBambinoDispersoRequest(int bambinoId, int gitaId, boolean isGita, long utcInstant) {
        super(bambinoId, gitaId, isGita, utcInstant, false);
    }
}
