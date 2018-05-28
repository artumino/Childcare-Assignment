package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwned;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwner;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Bambini")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Bambino extends Persona
{
    //region Relazioni

    @ManyToOne(fetch = FetchType.LAZY) //Non posso fare confronti se è LAZY :S
    @JoinColumn(name = "Pediatra_FK")
    private Pediatra pediatra;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TutoriLegali",
            joinColumns = { @JoinColumn(name = "Bambino_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Genitore_FK") }
    )
    private Set<Genitore> genitori = new HashSet<>();

    @ManyToMany(mappedBy = "bambini", fetch = FetchType.LAZY)
    private Set<Contatto> contatti = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Gruppo_FK")
    private Gruppo gruppo;

    //endregion

    //region Metodi

    public Bambino() { }

    public Bambino(String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, byte sesso)
    {
        super(nome, cognome, codiceFiscale, dataNascita, stato, comune, provincia, cittadinanza, residenza, sesso);
    }

    public Pediatra getPediatra() {
        return pediatra;
    }

    public void setPediatra(Pediatra pediatra) { this.pediatra = pediatra; }

    public Gruppo getGruppo() { return gruppo; }

    public void setGruppo(Gruppo gruppo) { this.gruppo = gruppo; }

    public void addGenitore(Genitore g) { genitori.add(g); }

    public void removeGenitore(Genitore g) { genitori.remove(g); }

    public void unsafeAddContatto(Contatto c) { contatti.add(c); }

    public void unsafeRemoveContatto(Contatto c) { contatti.remove(c); }

    public Set<Genitore> getGenitori() { return EntitiesHelper.unmodifiableListReturn(genitori); }

    public Set<Contatto> getContatti()
    {
        return EntitiesHelper.unmodifiableListReturn(contatti);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bambino)) return false;
        if (!super.equals(o)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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
        gruppo = DTOUtils.objectToDTO(gruppo,processed);
        pediatra = DTOUtils.objectToDTO(pediatra,processed);

        genitori = DTOUtils.iterableToDTO(genitori,processed);
        contatti = DTOUtils.iterableToDTO(contatti,processed);

        genitori = getGenitori();
        contatti = getContatti();

        super.toDTO(processed);
    }

    @Override
    public boolean isDTO() {
        return super.isDTO() && DTOUtils.isDTO(gruppo) && DTOUtils.isDTO(pediatra) && DTOUtils.isDTO(genitori) &&  DTOUtils.isDTO(contatti);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    //region Relations Interfaces

    public IManyToOne<Pediatra, Bambino> asBambinoPediatraRelation()
    {
        return new IManyToOne<Pediatra, Bambino>() {
            @Override
            public Bambino getItem() {
                return Bambino.this;
            }

            @Override
            public void setRelation(Pediatra item)
            {
                setPediatra(item);
            }

            @Override
            public Pediatra getRelation()
            {
                return getPediatra();
            }

            @Override
            public IOneToMany<Bambino, Pediatra> getInverse(Pediatra item) {
                return null;
            }
        };
    }

    public IManyToManyOwned<Contatto, Bambino> asBambinoContattoRelation()
    {
        return new IManyToManyOwned<Contatto, Bambino>() {
            @Override
            public Bambino getItem() {
                return Bambino.this;
            }

            @Override
            public void unsafeAddRelation(Contatto item) {
                unsafeAddContatto(item);
            }

            @Override
            public void unsafeRemoveRelation(Contatto item) {
                unsafeRemoveContatto(item);
            }

            @Override
            public Set<Contatto> getUnmodifiableRelation() {
                return getContatti();
            }

            @Override
            public IManyToManyOwner<Bambino, Contatto> getInverse(Contatto item)
            {
                return item.asContattiBambiniRelation();
            }
        };
    }

    public IManyToManyOwner<Genitore, Bambino> asBambinoGenitoreRelation()
    {
        return new IManyToManyOwner<Genitore, Bambino>() {
            @Override
            public Bambino getItem() {
                return Bambino.this;
            }

            @Override
            public void addRelation(Genitore item) {
                addGenitore(item);
            }

            @Override
            public void removeRelation(Genitore item) {
                removeGenitore(item);
            }

            @Override
            public Set<Genitore> getUnmodifiableRelation() {
                return getGenitori();
            }

            @Override
            public IManyToManyOwned<Bambino, Genitore> getInverse(Genitore item) {
                return null;
            }
        };
    }

    //endregion
}
