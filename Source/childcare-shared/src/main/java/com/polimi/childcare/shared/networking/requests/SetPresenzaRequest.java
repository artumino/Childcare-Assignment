package com.polimi.childcare.shared.networking.requests;

public class SetPresenzaRequest extends BaseRequest
{
    private int bambinoId;
    private int gitaId;
    private boolean isGita;
    private long utcInstant;
    private boolean uscita;

    public SetPresenzaRequest(int bambinoId, int gitaId, boolean isGita, long utcInstant, boolean uscita)
    {
        this.bambinoId = bambinoId;
        this.gitaId = gitaId;
        this.isGita = isGita;
        this.utcInstant = utcInstant;
        this.uscita = uscita;
    }

    public int getBambinoId() {
        return bambinoId;
    }

    public int getGitaId() {
        return gitaId;
    }

    public boolean isGita() {
        return isGita;
    }

    public long getUtcInstant() {
        return utcInstant;
    }

    /**
     * @return true in caso di uscita, false in caso di ingresso
     */
    public boolean isUscita() {
        return uscita;
    }
}
