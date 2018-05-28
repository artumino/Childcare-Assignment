package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.util.*;

@Entity
@DiscriminatorValue(value = "1")
public class Pediatra extends Contatto
{
    //region Relazioni

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pediatra")
    private Set<Bambino> bambini = new HashSet<>();

    //endregion

    //region Metodi

    public Pediatra() { }

    public Pediatra(String descrizione, String nome, String cognome, String indirizzo) {
        super(descrizione, nome, cognome, indirizzo);
    }

    public void unsafeAddBambino(Bambino b) { bambini.add(b); }

    public void unsafeRemoveBambino(Bambino b) { bambini.remove(b); }

    public Set<Bambino> getBambiniCurati() { return EntitiesHelper.unmodifiableListReturn(bambini); }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
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
    public void toDTO(List<Object> processed)
    {
        bambini = DTOUtils.iterableToDTO(bambini, processed);
        bambini = getBambini();

        super.toDTO(processed);
    }

    @Override
    public boolean isDTO() {
        return super.isDTO() && DTOUtils.isDTO(bambini);
    }

    //endregion

    //region Relations Interfaces

    public IOneToMany<Bambino, Pediatra> asPediatraBambiniRelation()
    {
        return new IOneToMany<Bambino, Pediatra>() {
            @Override
            public Pediatra getItem() {
                return Pediatra.this;
            }

            @Override
            public void unsafeAddRelation(Bambino item) {
                unsafeAddBambino(item);
            }

            @Override
            public void unsafeRemoveRelation(Bambino item) {
                unsafeRemoveBambino(item);
            }

            @Override
            public Set<Bambino> getUnmodifiableRelation() {
                return getBambiniCurati();
            }

            @Override
            public IManyToOne<Pediatra, Bambino> getInverse(Bambino item) {
                return item.asBambiniPediatraRelation();
            }
        };
    }
    //endregion
}
