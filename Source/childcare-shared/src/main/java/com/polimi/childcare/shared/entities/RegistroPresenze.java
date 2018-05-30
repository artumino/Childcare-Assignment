package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
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
    @Column(nullable = false)
    private StatoPresenza Stato;

    @Column(nullable = false)
    private LocalDate Date;

    @Column(nullable = false)
    private LocalDateTime TimeStamp;

    @Column(nullable = false) //Ma short non è nullable di suo :S
    private short Ora;

    //endregion

    //region Relazioni

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="Bambino_FK")
    private Bambino bambino;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "Gita_FK")
    private Gita gita;

    //endregion

    //region Metodi

    public RegistroPresenze() { }

    public RegistroPresenze(StatoPresenza stato, LocalDate date, LocalDateTime timeStamp, short ora, Bambino bambino)
    {
        Stato = stato;
        Date = date;
        TimeStamp = timeStamp;
        Ora = ora;
        this.bambino = bambino;
    }

    public RegistroPresenze(StatoPresenza stato, LocalDate date, LocalDateTime timeStamp, short ora, Bambino bambino, Gita gita)
    {
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

    public LocalDate getDate() { return Date; }

    public void setDate(LocalDate date) { Date = date; }

    public LocalDateTime getTimeStamp() { return TimeStamp; }

    public void setTimeStamp(LocalDateTime timeStamp) { TimeStamp = timeStamp; }

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
        Presente(), Assente(), EntratoInRitardo(), UscitoInAnticipo(), Uscito(), Disperso();
    }

    //endregion

    //region DTO


    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO(List<Object> processed)
    {
        bambino = DTOUtils.objectToDTO(bambino, processed);
        gita = DTOUtils.objectToDTO(gita, processed);
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(bambino) && DTOUtils.isDTO(gita);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion


    //region Relations Interfaces

    public IManyToOne<Bambino, RegistroPresenze> asRegistroPresenzeBambinoRelation()
    {
        return new IManyToOne<Bambino, RegistroPresenze>() {
            @Override
            public RegistroPresenze getItem() {
                return RegistroPresenze.this;
            }

            @Override
            public void setRelation(Bambino item) {
                setBambino(item);
            }

            @Override
            public Bambino getRelation() {
                return getBambino();
            }

            @Override
            public IOneToMany<RegistroPresenze, Bambino> getInverse(Bambino item) {
                return null; //CASO SPECIALE
            }
        };
    }

    public IManyToOne<Gita, RegistroPresenze> asRegistroPresenzeGitaRelation()
    {
        return new IManyToOne<Gita, RegistroPresenze>() {
            @Override
            public RegistroPresenze getItem() {
                return RegistroPresenze.this;
            }

            @Override
            public void setRelation(Gita item) {
                setGita(item);
            }

            @Override
            public Gita getRelation() {
                return getGita();
            }

            @Override
            public IOneToMany<RegistroPresenze, Gita> getInverse(Gita item) {
                return item.asGitaRegistroPresenzeRelation();
            }
        };
    }

    //endregion
}
