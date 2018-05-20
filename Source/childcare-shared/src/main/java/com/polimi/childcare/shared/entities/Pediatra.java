package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@DiscriminatorValue(value = "1")
public class Pediatra extends Contatto
{
    //region Relazioni

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pediatra")
    private Set<Bambino> bambini = new HashSet<>();

    //endregion

    //region Metodi

    public Pediatra() { }

    public Pediatra(String descrizione, String nome, String cognome, String indirizzo) {
        super(descrizione, nome, cognome, indirizzo);
    }

    public Set<Bambino> getBambiniCurati() { return EntitiesHelper.unmodifiableListReturn(bambini); }

    //endregion
}
