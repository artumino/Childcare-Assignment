package com.polimi.childcare.client.android.tuples;

import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gruppo;
import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.time.LocalDate;

public class BambinoGruppoTuple
{
    private Bambino linkedBambino;
    private RegistroPresenze linkedPresenza;
    private Gruppo linkedGruppo;

    public BambinoGruppoTuple(Bambino linkedBambino) {
        this.linkedBambino = linkedBambino;
        this.linkedPresenza = new RegistroPresenze(RegistroPresenze.StatoPresenza.Assente, LocalDate.now(), LocalDate.now().atStartOfDay(), (short)0, this.linkedBambino);
    }

    public BambinoGruppoTuple(Bambino linkedBambino, RegistroPresenze linkedPresenza) {
        this.linkedBambino = linkedBambino;
        this.linkedPresenza = linkedPresenza;
    }

    public BambinoGruppoTuple(Bambino linkedBambino, Gruppo linkedGruppo) {
        this.linkedBambino = linkedBambino;
        this.linkedGruppo = linkedGruppo;
        this.linkedPresenza = new RegistroPresenze(RegistroPresenze.StatoPresenza.Assente, LocalDate.now(), LocalDate.now().atStartOfDay(), (short) 0, this.linkedBambino);
    }

    public BambinoGruppoTuple(Bambino linkedBambino, Gruppo linkedGruppo, RegistroPresenze linkedPresenza) {
        this.linkedBambino = linkedBambino;
        this.linkedGruppo = linkedGruppo;
        this.linkedPresenza = linkedPresenza;
    }

    public Bambino getLinkedBambino() {
        return linkedBambino;
    }

    public void setLinkedBambino(Bambino linkedBambino) {
        this.linkedBambino = linkedBambino;
    }

    public Gruppo getLinkedGruppo() {
        return linkedGruppo;
    }

    public void setLinkedGruppo(Gruppo linkedGruppo) {
        this.linkedGruppo = linkedGruppo;
    }

    public RegistroPresenze getLinkedPresenza() {
        return linkedPresenza;
    }

    public void setLinkedPresenza(RegistroPresenze linkedPresenza) {
        this.linkedPresenza = linkedPresenza;
    }

    @Override
    public int hashCode()
    {
        return linkedBambino.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof BambinoGruppoTuple))
            return false;
        return (linkedPresenza == ((BambinoGruppoTuple) obj).getLinkedPresenza() && (linkedPresenza == null || linkedPresenza.equals(((BambinoGruppoTuple) obj).getLinkedPresenza()))) &&
                (linkedBambino == ((BambinoGruppoTuple) obj).getLinkedBambino() && (linkedBambino == null || linkedBambino.equals(((BambinoGruppoTuple) obj).getLinkedBambino()))) &&
                (linkedGruppo == ((BambinoGruppoTuple) obj).getLinkedGruppo() && (linkedGruppo == null || linkedGruppo.equals(((BambinoGruppoTuple) obj).getLinkedGruppo())));
    }
}
