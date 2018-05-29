package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwned;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwner;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Genitori")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Genitore extends Persona
{
    //region Relazioni

    @ManyToMany(mappedBy = "genitori", fetch = FetchType.LAZY)
    private Set<Bambino> bambini = new HashSet<>();

    //endregion

    //region Metodi

    public Genitore() { }

    public Genitore(String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, ESesso sesso) {
        super(nome, cognome, codiceFiscale, dataNascita, stato, comune, provincia, cittadinanza, residenza, sesso);
    }

    public void unsafeAddBambino(Bambino b) { bambini.add(b); }

    public void unsafeRemoveBambino(Bambino b) { bambini.remove(b); }

    public Set<Bambino> getBambini() { return EntitiesHelper.unmodifiableListReturn(bambini); }

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

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    public IManyToManyOwned<Bambino, Genitore> asGenitoriBambiniRelation()
    {
        return new IManyToManyOwned<Bambino,Genitore>() {
            @Override
            public Genitore getItem() {
                return Genitore.this;
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
                return getBambini();
            }

            @Override
            public IManyToManyOwner<Genitore, Bambino> getInverse(Bambino item) {
                return item.asBambiniGenitoriRelation();
            }
        };
    }
}
