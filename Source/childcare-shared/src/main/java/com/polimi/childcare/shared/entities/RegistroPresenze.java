package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "RegistroPresenze")
public class RegistroPresenze extends TransferableEntity implements Serializable
{
    //region Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Enumerated(EnumType.ORDINAL)
    private StatoPresenza Stato;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date Date;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date TimeStamp;

    @Column(nullable = false)
    private short Ora;

    //endregion

    //region Relazioni

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="Bambino_FK")
    private Bambino bambino;

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "Gita_FK")
    private Gita gita;

    //endregion

    //region Metodi

    public RegistroPresenze() { }

    public RegistroPresenze(StatoPresenza stato, java.util.Date date, java.util.Date timeStamp, short ora, Bambino bambino, Gita gita) {
        Stato = stato;
        Date = date;
        TimeStamp = timeStamp;
        Ora = ora;
        this.bambino = bambino;
        this.gita = gita;
    }

    public int getID() {
        return ID;
    }

    public StatoPresenza getStato() {
        return Stato;
    }

    public void setStato(StatoPresenza stato) {
        Stato = stato;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public java.util.Date getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(java.util.Date timeStamp) {
        TimeStamp = timeStamp;
    }

    public short getOra() {
        return Ora;
    }

    public void setOra(short ora) {
        Ora = ora;
    }

    public Bambino getBambino() {
        return bambino;
    }

    public void setBambino(Bambino bambino) {
        this.bambino = bambino;
    }

    public Gita getGita() {
        return gita;
    }

    public void setGita(Gita gita) {
        this.gita = gita;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, RegistroPresenze.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistroPresenze)) return false;
        RegistroPresenze that = (RegistroPresenze) o;
        return getID() == that.getID() &&
                getOra() == that.getOra() &&
                getStato() == that.getStato() &&
                getDate().compareTo(that.getDate()) == 0 &&
                getTimeStamp().compareTo(that.getTimeStamp()) == 0;
    }

    //endregion

    //region Enumeratori

    public enum StatoPresenza
    {
        Presente(), Assente(), EntratoInRitardo(), UscitoInAnticipo(), Disperso();
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
        bambino = DTOUtils.objectToDTO(bambino);
        gita = DTOUtils.objectToDTO(gita);
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(bambino) && DTOUtils.isDTO(gita);
    }

    //endregion
}
