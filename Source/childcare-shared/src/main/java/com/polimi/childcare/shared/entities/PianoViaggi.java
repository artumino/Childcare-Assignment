package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Gita_FK")
    private Gita gita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Gruppo_FK")
    private Gruppo gruppo;

    @Column(name = "Gruppo_FK", insertable = false, updatable = false)
    private int gruppo_fk;

    @ManyToOne(fetch = FetchType.EAGER)
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

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
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

    public int getGruppoForeignKey() {
        return gruppo_fk;
    }

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
    public void toDTO(List<Object> processed)
    {
        gruppo = DTOUtils.objectToDTO(gruppo, processed);
        mezzo = DTOUtils.objectToDTO(mezzo, processed);
        gita = DTOUtils.objectToDTO(gita, processed);
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(gruppo) && DTOUtils.isDTO(mezzo) && DTOUtils.isDTO(gita);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion


    //region Relations Interfaces

    public IManyToOne<Gita, PianoViaggi> asPianiViaggioGitaRelation()
    {
        return new IManyToOne<Gita, PianoViaggi>() {
            @Override
            public PianoViaggi getItem() {
                return PianoViaggi.this;
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
            public IOneToMany<PianoViaggi, Gita> getInverse(Gita item) {
                return item.asGitaPianiViaggioRelation();
            }
        };
    }

    public IManyToOne<Gruppo, PianoViaggi> asPianiViaggioGruppoRelation()
    {
        return new IManyToOne<Gruppo, PianoViaggi>() {
            @Override
            public PianoViaggi getItem() {
                return PianoViaggi.this;
            }

            @Override
            public void setRelation(Gruppo item) {
                setGruppo(item);
            }

            @Override
            public Gruppo getRelation() {
                return getGruppo();
            }

            @Override
            public IOneToMany<PianoViaggi, Gruppo> getInverse(Gruppo item) {
                return item.asGruppoPianiViaggioRelation();
            }
        };
    }

    public IManyToOne<MezzoDiTrasporto, PianoViaggi> asPianiViaggioMezzoDiTrasportoRelation()
    {
        return new IManyToOne<MezzoDiTrasporto, PianoViaggi>() {
            @Override
            public PianoViaggi getItem() {
                return PianoViaggi.this;
            }

            @Override
            public void setRelation(MezzoDiTrasporto item) {
                setMezzo(item);
            }

            @Override
            public MezzoDiTrasporto getRelation() {
                return getMezzo();
            }

            @Override
            public IOneToMany<PianoViaggi, MezzoDiTrasporto> getInverse(MezzoDiTrasporto item) {
                return item.asMezzoDiTrasportoPianiViaggo();
            }
        };
    }

    //endregion
}
