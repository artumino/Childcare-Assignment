package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue(value = "1")
public class Pediatra extends Contatto
{
    //region Relazioni

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pediatra")
    private List<Bambino> bambini;

    //endregion

    //region Metodi

    public Pediatra() { }

    public Pediatra(String descrizione, String nome, String cognome, String indirizzo) {
        super(descrizione, nome, cognome, indirizzo);
    }

    public void addBambinoCurato(Bambino b)
    {
        if(bambini == null)
            bambini = new ArrayList<>();
        bambini.add(b);
    }   //Poi va fatto update del Database

    public void removeBambinoCurato(Bambino b)
    {
        if(bambini != null)
            bambini.remove(b);
    }   //Poi va fatto update del Database

    public List<Bambino> getBambiniCurati() { return bambini; }

    //endregion
}
