package com.polimi.childcare.shared.entities;

import com.polimi.childcare.shared.dto.DTOUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Genitori")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Genitore extends Persona
{
    //region Relazioni

    @ManyToMany(mappedBy = "genitori", fetch = FetchType.EAGER) //Ora va :D
    private Set<Bambino> bambini = new HashSet<>(); //Non fa nulla

    //endregion

    //region Metodi

    public Genitore() { }

    public Genitore(String nome, String cognome, String codiceFiscale, Date dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, byte sesso) {
        super(nome, cognome, codiceFiscale, dataNascita, stato, comune, provincia, cittadinanza, residenza, sesso);
    }

    public Set<Bambino> getBambini()
    {
        Set<Bambino> ritorno = new HashSet<>(bambini);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
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
        bambini = getBambini();

        DTOUtils.iterableToDTO(bambini);

        super.toDTO();
    }

    @Override
    public boolean isDTO() {
        return super.isDTO() && (bambini instanceof HashSet);
    }

    //endregion
}
