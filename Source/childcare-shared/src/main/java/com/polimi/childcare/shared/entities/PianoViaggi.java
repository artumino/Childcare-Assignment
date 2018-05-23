package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PianiViaggi")
public class PianoViaggi extends TransferableEntity implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Gita_FK")
    private Gita gita;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Gruppo_FK")
    private Gruppo gruppo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Mezzo_FK")
    private MezzoDiTrasporto mezzo;

    //endregion

    //region Metodi

    public PianoViaggi() { }

    public PianoViaggi(Gita gita, Gruppo gruppo, MezzoDiTrasporto mezzo) {
        this.gita = gita;
        this.gruppo = gruppo;
        this.mezzo = mezzo;
    }

    public int getID() {
        return ID;
    }

    public Gita getGita() {
        return gita;
    }

    public void setGita(Gita gita) {
        this.gita = gita;
    }

    public Gruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(Gruppo gruppo) {
        this.gruppo = gruppo;
    }

    public MezzoDiTrasporto getMezzo() {
        return mezzo;
    }

    public void setMezzo(MezzoDiTrasporto mezzo) { this.mezzo = mezzo; }

    @Override
    public int hashCode() { return Objects.hash(ID, PianoViaggi.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PianoViaggi)) return false;
        PianoViaggi that = (PianoViaggi) o;
        return getID() == that.getID();
    }

    //endregion

    //region DTO


    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO()
    {
        gruppo = DTOUtils.objectToDTO(gruppo);
        mezzo = DTOUtils.objectToDTO(mezzo);
        gita = DTOUtils.objectToDTO(gita);
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(gruppo) && DTOUtils.isDTO(mezzo) && DTOUtils.isDTO(gita);
    }

    //endregion
}
