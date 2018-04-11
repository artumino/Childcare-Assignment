package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue(value = "1")
public class Pediatra extends Contatto
{
    //region Relazioni

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "pediatra")
    private List<Bambino> bambini = new ArrayList<>();

    //endregion

    //region Metodi

    public Pediatra() { }

    public Pediatra(String descrizione, String nome, String cognome, String indirizzo) {
        super(descrizione, nome, cognome, indirizzo);
    }

    public List<Bambino> getBambiniCurati()
    {
        List<Bambino> ritorno = new ArrayList<>();
        Collections.copy(ritorno, bambini);
        Collections.unmodifiableList(ritorno);
        return ritorno;
    }

    //endregion
}
