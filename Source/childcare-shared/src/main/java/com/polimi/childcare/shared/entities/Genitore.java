package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Genitori")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Genitore extends Persona
{
    //region Relazioni

    @ManyToMany(mappedBy = "genitori")
    private Set<Bambino> bambini;

    //endregion

    //region Metodi

    public Genitore() { }

    public Genitore(String nome, String cognome, String codiceFiscale, Date dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, byte sesso) {
        super(nome, cognome, codiceFiscale, dataNascita, stato, comune, provincia, cittadinanza, residenza, sesso);
    }

    public void addBambino(Bambino b)
    {
        if(bambini == null)
            bambini = new HashSet<>();
        bambini.add(b);
    }   //Poi va fatto update del Database

    public void removeBambino(Bambino b)
    {
        if(bambini != null)
            bambini.remove(b);
    }   //Poi va fatto update del Database*/

    public Set<Bambino> getBambini() { return bambini; }

    //endregion
}
