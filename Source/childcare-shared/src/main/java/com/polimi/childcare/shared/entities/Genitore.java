package com.polimi.childcare.shared.entities;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;

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
}
