package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Menu")
public class Menu extends TransferableEntity implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    private String Nome;

    @Column(nullable = false)
    private boolean Attivo;

    @Column(nullable = false)
    private int Ricorrenza;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "menu")
    private Set<QuantitaPasto> quantitaPasto = new HashSet<>();

    //endregion

    //region Metodi

    public Menu() { }

    public Menu(String nome, boolean attivo, int ricorrenza)
    {
        Nome = nome;
        this.Attivo = attivo;
        Ricorrenza = ricorrenza;
    }

    public int getID() {
        return ID;
    }

    public boolean isAttivo() { return Attivo; }

    public void setAttivo(boolean attivo) { this.Attivo = attivo; }

    public int getRicorrenza() { return Ricorrenza; }

    public void setRicorrenza(int ricorrenza) { Ricorrenza = ricorrenza; }

    public String getNome() { return Nome; }

    public void setNome(String nome) { Nome = nome; }

    public void unsafeAddQuantitaPasto(QuantitaPasto q) { quantitaPasto.add(q); }

    public void unsafeRemoveQuantitaPasto(QuantitaPasto q) { quantitaPasto.remove(q); }

    public Set<QuantitaPasto> getQuantitaPasto() { return EntitiesHelper.unmodifiableListReturn(quantitaPasto); }

    @Override
    public int hashCode() { return Objects.hash(ID, Menu.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return getID() == menu.getID() &&
                getRicorrenza() == menu.getRicorrenza() &&
                isAttivo() == menu.isAttivo();
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
        quantitaPasto = DTOUtils.iterableToDTO(quantitaPasto, processed);
        quantitaPasto = this.getQuantitaPasto();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(quantitaPasto);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion
}
