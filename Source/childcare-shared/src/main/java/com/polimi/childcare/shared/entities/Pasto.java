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
@Table(name = "Pasti")
public class Pasto extends TransferableEntity implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 65)
    private String Nome;

    @Column(length = 250)
    private String Descrizione;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "Fornitore_FK")
    private Fornitore fornitore;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ReazioneAvversa_Pasto",
            joinColumns = { @JoinColumn(name = "Pasto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "ReazioneAvversa_FK") }
    )
    private Set<ReazioneAvversa> reazione = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pasto")
    private Set<QuantitaPasto> quantitaPasto = new HashSet<>();

    //endregion

    //region Metodi

    public Pasto() { }

    public Pasto(String nome, String descrizione)
    {
        Nome = nome;
        Descrizione = descrizione;
    }

    public int getID() {
        return ID;
    }

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
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

    public void setFornitore(Fornitore fornitore) { this.fornitore = fornitore; }

    public Fornitore getFornitore() { return fornitore; }

    public void addReazione(ReazioneAvversa r) { reazione.add(r); }

    public void removeReazione(ReazioneAvversa r) { reazione.remove(r); }

    public void unsafeAddQuantitaPasto(QuantitaPasto q) { quantitaPasto.add(q); }

    public void unsafeRemoveQuantitaPasto(QuantitaPasto q) { quantitaPasto.remove(q); }

    public Set<ReazioneAvversa> getReazione() { return EntitiesHelper.unmodifiableListReturn(reazione); }

    public Set<QuantitaPasto> getQuantitaPasto() { return EntitiesHelper.unmodifiableListReturn(quantitaPasto); }

    @Override
    public int hashCode() { return Objects.hash(ID, Pasto.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pasto)) return false;
        Pasto pasto = (Pasto) o;
        return getID() == pasto.getID() &&
                getNome().equals(pasto.getNome()) &&
                getDescrizione().equals(pasto.getDescrizione());
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
        fornitore = DTOUtils.objectToDTO(fornitore, processed);
        reazione = DTOUtils.iterableToDTO(reazione, processed);
        quantitaPasto = DTOUtils.iterableToDTO(quantitaPasto, processed);

        reazione = this.getReazione();
        quantitaPasto = this.getQuantitaPasto();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(fornitore) && DTOUtils.isDTO(reazione) && DTOUtils.isDTO(quantitaPasto);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    //region Relations Interfaces

    public IManyToOne<Fornitore, Pasto> asPastoFornitoreRelation()
    {
        return new IManyToOne<Fornitore, Pasto>() {
            @Override
            public Pasto getItem() {
                return Pasto.this;
            }

            @Override
            public void setRelation(Fornitore item) {
                setFornitore(item);
            }

            @Override
            public Fornitore getRelation() {
                return getFornitore();
            }

            @Override
            public IOneToMany<Pasto, Fornitore> getInverse(Fornitore item) {
                return item.asFornitorePastiRelation();
            }
        };
    }

    public IManyToManyOwner<ReazioneAvversa, Pasto> asPastoReazioniAvverseRelation()
    {
        return new IManyToManyOwner<ReazioneAvversa, Pasto>() {
            @Override
            public Pasto getItem() {
                return Pasto.this;
            }

            @Override
            public void addRelation(ReazioneAvversa item) {
                addReazione(item);
            }

            @Override
            public void removeRelation(ReazioneAvversa item) {
                removeReazione(item);
            }

            @Override
            public Set<ReazioneAvversa> getUnmodifiableRelation() {
                return getReazione();
            }

            @Override
            public IManyToManyOwned<Pasto, ReazioneAvversa> getInverse(ReazioneAvversa item) {
                return item.asReazioniAvversePastiRelation();
            }
        };
    }

    public IOneToMany<QuantitaPasto, Pasto> asPastoQuantitaPastiRelation()
    {
        return new IOneToMany<QuantitaPasto, Pasto>() {
            @Override
            public Pasto getItem() {
                return Pasto.this;
            }

            @Override
            public void unsafeAddRelation(QuantitaPasto item) {
                unsafeAddQuantitaPasto(item);
            }

            @Override
            public void unsafeRemoveRelation(QuantitaPasto item) {
                unsafeRemoveQuantitaPasto(item);
            }

            @Override
            public Set<QuantitaPasto> getUnmodifiableRelation() {
                return getQuantitaPasto();
            }

            @Override
            public IManyToOne<Pasto, QuantitaPasto> getInverse(QuantitaPasto item) {
                return item.asQuantitaPastiPastoRelation();
            }
        };
    }


    //endregion
}
