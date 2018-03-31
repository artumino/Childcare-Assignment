package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Genitori")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Genitore extends Persona
{
    //region Relazioni

    @ManyToMany(mappedBy = "genitori")
    private List<Bambino> bambini;

    //endregion

    //region Metodi

    public Genitore() { }

    public Genitore(String nome, String cognome, String codiceFiscale, Date dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, byte sesso) {
        super(nome, cognome, codiceFiscale, dataNascita, stato, comune, provincia, cittadinanza, residenza, sesso);
    }

    public void addBambino(Bambino b)
    {
        if(bambini == null)
            bambini = new ArrayList<>();
        bambini.add(b);
    }   //Poi va fatto update del Database

    public void removeBambino(Bambino b)
    {
        if(bambini != null)
            bambini.remove(b);
    }   //Poi va fatto update del Database

    public List<Bambino> getBambini() { return bambini; }

    //endregion
}
