package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
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

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY) //Non posso fare confronti se è LAZY :S
    @JoinColumn(name = "Pediatra_FK")
    private Pediatra pediatra;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "TutoriLegali",
            joinColumns = { @JoinColumn(name = "Bambino_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Genitore_FK") }
    )
    private Set<Genitore> genitori = new HashSet<>();

    @ManyToMany(mappedBy = "bambini", fetch = FetchType.LAZY)
    private Set<Contatto> contatti = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
    public void toDTO()
    {
        gruppo = DTOUtils.objectToDTO(gruppo);
        pediatra = DTOUtils.objectToDTO(pediatra);

        genitori = DTOUtils.iterableToDTO(genitori);
        contatti = DTOUtils.iterableToDTO(contatti);

        genitori = getGenitori();
        contatti = getContatti();

        super.toDTO();
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
}
