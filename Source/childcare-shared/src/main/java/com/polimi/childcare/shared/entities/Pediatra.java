package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
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

    //region DTO


    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO()
    {
        bambini = DTOUtils.iterableToDTO(bambini);
        bambini = getBambini();

        super.toDTO();
    }

    @Override
    public boolean isDTO() {
        return super.isDTO() && DTOUtils.isDTO(bambini);
    }

    //endregion
}
