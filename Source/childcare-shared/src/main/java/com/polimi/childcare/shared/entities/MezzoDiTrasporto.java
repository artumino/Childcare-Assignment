package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "MezziDiTrasporto")
public class MezzoDiTrasporto implements Serializable, ITransferable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 7)   //Targa Italiana
    private String Targa;

    @Column(nullable = false)
    private int Capienza;

    @Column(nullable = false)
    private int NumeroIdentificativo;

    @Column(nullable = false)
    private int CostoOrario;

    //endregion Attributi

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Fornitore_FK")
    private Fornitore fornitore;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mezzo")
    private Set<PianoViaggi> pianoViaggi = new HashSet<>();

    //endregion

    //region Metodi

    public MezzoDiTrasporto() { }

    public MezzoDiTrasporto(String targa, int capienza, int numeroIdentificativo, int costoOrario, Fornitore fornitore) {
        Targa = targa;
        Capienza = capienza;
        NumeroIdentificativo = numeroIdentificativo;
        CostoOrario = costoOrario;
        this.fornitore = fornitore;
    }

    public int getID() {
        return ID;
    }

    public String getTarga() {
        return Targa;
    }

    public void setTarga(String targa) {
        Targa = targa;
    }

    public int getCapienza() {
        return Capienza;
    }

    public void setCapienza(int capienza) {
        Capienza = capienza;
    }

    public int getNumeroIdentificativo() {
        return NumeroIdentificativo;
    }

    public void setNumeroIdentificativo(int numeroIdentificativo) {
        NumeroIdentificativo = numeroIdentificativo;
    }

    public int getCostoOrario() {
        return CostoOrario;
    }

    public void setCostoOrario(int costoOrario) {
        CostoOrario = costoOrario;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    public Set<PianoViaggi> getPianoViaggi()
    {
        Set<PianoViaggi> ritorno = new HashSet<>(pianoViaggi);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, MezzoDiTrasporto.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MezzoDiTrasporto)) return false;
        MezzoDiTrasporto that = (MezzoDiTrasporto) o;
        return getID() == that.getID() &&
                getCapienza() == that.getCapienza() &&
                getNumeroIdentificativo() == that.getNumeroIdentificativo() &&
                getCostoOrario() == that.getCostoOrario() &&
                getTarga().equals(that.getTarga());
                //getFornitore().equals(that.getFornitore());
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
        pianoViaggi = getPianoViaggi();

        DTOUtils.objectToDTO(fornitore);

        DTOUtils.iterableToDTO(pianoViaggi);
    }

    @Override
    public boolean isDTO()
    {
        return (pianoViaggi instanceof HashSet) && DTOUtils.isDTO(fornitore);
    }

    //endregion
}
