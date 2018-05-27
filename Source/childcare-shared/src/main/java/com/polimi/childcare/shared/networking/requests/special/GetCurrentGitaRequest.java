package com.polimi.childcare.shared.networking.requests.special;

import com.polimi.childcare.shared.networking.requests.BaseRequest;

/**
 * Richiede la attuale gita in questo periodo (per evitare possibili errori, è ritornata una lista di gite in caso di
 * gite multiple che si sovrappongono)
 */
public class GetCurrentGitaRequest extends BaseRequest
{
    private long utcInstantEpochSeconds;

    public GetCurrentGitaRequest(long utcInstantEpochSeconds) {
        this.utcInstantEpochSeconds = utcInstantEpochSeconds;
    }

    public long getUtcInstantEpochSeconds() {
        return utcInstantEpochSeconds;
    }
}
