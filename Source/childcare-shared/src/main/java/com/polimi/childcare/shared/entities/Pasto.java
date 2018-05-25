package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
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

    @Column(nullable = false, length = 20)
    private String Nome;

    @Column(length = 50)
    private String Descrizione;

    //endregion

    //region Relazioni

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Fornitore_Pasto",
            joinColumns = { @JoinColumn(name = "Pasto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Fornitore_FK") }
    )
    private Set<Fornitore> fornitori = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "ReazioneAvversa_Pasto",
            joinColumns = { @JoinColumn(name = "Pasto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "ReazioneAvversa_FK") }
    )
    private Set<ReazioneAvversa> reazione = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "pasto")
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

    public void addFornitore(Fornitore f) { fornitori.add(f); }

    public void removeFornitore(Fornitore f) { fornitori.remove(f); }

    public void addReazione(ReazioneAvversa r) { reazione.add(r); }

    public void removeReazione(ReazioneAvversa r) { reazione.remove(r); }

    public void unsafeAddQuantitaPasto(QuantitaPasto q) { quantitaPasto.add(q); }

    public void unsafeRemoveQuantitaPasto(QuantitaPasto q) { quantitaPasto.remove(q); }

    public Set<Fornitore> getFornitori() { return EntitiesHelper.unmodifiableListReturn(fornitori); }

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
    public void toDTO()
    {
        //Aggiorna figli
        fornitori = DTOUtils.iterableToDTO(fornitori);
        reazione = DTOUtils.iterableToDTO(reazione);
        quantitaPasto = DTOUtils.iterableToDTO(quantitaPasto);

        fornitori = this.getFornitori();
        reazione = this.getReazione();
        quantitaPasto = this.getQuantitaPasto();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(fornitori) && DTOUtils.isDTO(reazione) && DTOUtils.isDTO(quantitaPasto);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion
}
