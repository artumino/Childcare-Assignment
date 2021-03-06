package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "MezziDiTrasporto")
public class MezzoDiTrasporto extends TransferableEntity implements Serializable
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "Fornitore_FK")
    private Fornitore fornitore;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mezzo")
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

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
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

    public void unsafeAddPianoViaggi(PianoViaggi p) { pianoViaggi.add(p); }

    public void unsafeRemovePianoViaggi(PianoViaggi p) { pianoViaggi.remove(p); }

    public Set<PianoViaggi> getPianoViaggi() { return EntitiesHelper.unmodifiableListReturn(pianoViaggi); }

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
        fornitore = DTOUtils.objectToDTO(fornitore, processed);
        pianoViaggi = DTOUtils.iterableToDTO(pianoViaggi, processed);

        pianoViaggi = getPianoViaggi();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(pianoViaggi) && DTOUtils.isDTO(fornitore);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    //region Relations Interfaces

    public IManyToOne<Fornitore, MezzoDiTrasporto> asMezziDiTrasportoFornitoreRelation()
    {
        return new IManyToOne<Fornitore, MezzoDiTrasporto>() {
            @Override
            public MezzoDiTrasporto getItem() {
                return MezzoDiTrasporto.this;
            }

            @Override
            public void setRelation(Fornitore item) {
                setFornitore(item);
            }

            @Override
            public Fornitore getRelation() {
                return getFornitore();
            }

            @Override
            public IOneToMany<MezzoDiTrasporto, Fornitore> getInverse(Fornitore item) {
                return item.asFornitoreMezziDiTrasportoRelation();
            }
        };
    }

    public IOneToMany<PianoViaggi, MezzoDiTrasporto> asMezzoDiTrasportoPianiViaggo()
    {
        return new IOneToMany<PianoViaggi, MezzoDiTrasporto>() {
            @Override
            public MezzoDiTrasporto getItem() {
                return MezzoDiTrasporto.this;
            }

            @Override
            public void unsafeAddRelation(PianoViaggi item) {
                unsafeAddPianoViaggi(item);
            }

            @Override
            public void unsafeRemoveRelation(PianoViaggi item) {
                unsafeRemovePianoViaggi(item);
            }

            @Override
            public Set<PianoViaggi> getUnmodifiableRelation() {
                return getPianoViaggi();
            }

            @Override
            public IManyToOne<MezzoDiTrasporto, PianoViaggi> getInverse(PianoViaggi item) {
                return item.asPianiViaggioMezzoDiTrasportoRelation();
            }
        };
    }

    //endregion
}
