package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    //endregion
}
