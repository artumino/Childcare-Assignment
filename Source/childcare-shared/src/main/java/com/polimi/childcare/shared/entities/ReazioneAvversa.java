package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwned;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwner;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "ReazioniAvverse")
public class ReazioneAvversa extends TransferableEntity implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 20)
    private String Nome;

    @Column(length = 250)
    private String Descrizione;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "reazioneAvversa")
    private Set<Diagnosi> diagnosi = new HashSet<>();

    @ManyToMany(mappedBy = "reazione", fetch = FetchType.EAGER)
    private Set<Pasto> pasti = new HashSet<>();

    //endregion

    //region Metodi

    public ReazioneAvversa() { }

    public ReazioneAvversa(String nome, String descrizione) {
        Nome = nome;
        Descrizione = descrizione;
    }

    public int getID() {
        return ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public void unsafeAddDiagnosi(Diagnosi d) { diagnosi.add(d); }

    public void unsafeRemoveDiagnosi(Diagnosi d) { diagnosi.remove(d); }

    public void unsafeAddPasto(Pasto p) { pasti.add(p); }

    public void unsafeRemovePasto(Pasto p) { pasti.remove(p); }

    public Set<Diagnosi> getDiagnosi() { return EntitiesHelper.unmodifiableListReturn(diagnosi); }

    public Set<Pasto> getPasti() { return EntitiesHelper.unmodifiableListReturn(pasti); }

    @Override
    public int hashCode() { return Objects.hash(ID, ReazioneAvversa.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReazioneAvversa)) return false;
        ReazioneAvversa that = (ReazioneAvversa) o;
        return getID() == that.getID() &&
                getNome().equals(that.getNome()) &&
                getDescrizione().equals(that.getDescrizione());
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
        //Aggiorna figli
        pasti = DTOUtils.iterableToDTO(pasti, processed);
        diagnosi = DTOUtils.iterableToDTO(diagnosi, processed);

        pasti = this.getPasti();
        diagnosi = this.getDiagnosi();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(pasti) && DTOUtils.isDTO(diagnosi);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion


    //region Relations Interfaces

    public IOneToMany<Diagnosi, ReazioneAvversa> asReazioniAvverseDiagnosiRelation()
    {
        return new IOneToMany<Diagnosi, ReazioneAvversa>() {
            @Override
            public ReazioneAvversa getItem() {
                return ReazioneAvversa.this;
            }

            @Override
            public void unsafeAddRelation(Diagnosi item) {
                unsafeAddDiagnosi(item);
            }

            @Override
            public void unsafeRemoveRelation(Diagnosi item) {
                unsafeRemoveDiagnosi(item);
            }

            @Override
            public Set<Diagnosi> getUnmodifiableRelation() {
                return getDiagnosi();
            }

            @Override
            public IManyToOne<ReazioneAvversa, Diagnosi> getInverse(Diagnosi item) {
                return item.asDiagnosiReazioneAvversaRelation();
            }
        };
    }

    public IManyToManyOwned<Pasto, ReazioneAvversa> asReazioniAvversePastiRelation()
    {
        return new IManyToManyOwned<Pasto, ReazioneAvversa>() {
            @Override
            public ReazioneAvversa getItem() {
                return ReazioneAvversa.this;
            }

            @Override
            public void unsafeAddRelation(Pasto item) {
                unsafeAddPasto(item);
            }

            @Override
            public void unsafeRemoveRelation(Pasto item) {
                unsafeRemovePasto(item);
            }

            @Override
            public Set<Pasto> getUnmodifiableRelation() {
                return getPasti();
            }

            @Override
            public IManyToManyOwner<ReazioneAvversa, Pasto> getInverse(Pasto item) {
                return item.asPastoReazioniAvverseRelation();
            }
        };
    }

    //endregion
}
