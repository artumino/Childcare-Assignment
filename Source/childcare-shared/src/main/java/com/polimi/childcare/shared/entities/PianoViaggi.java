package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PianiViaggi")
public class PianoViaggi implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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

    public void setMezzo(MezzoDiTrasporto mezzo) {
        this.mezzo = mezzo;
    }

    //endregion
}
