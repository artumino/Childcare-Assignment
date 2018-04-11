package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Menu")
public class Menu implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date DataInizio;

    @Column(nullable = false)
    private int Ricorrenza;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "menu")
    private List<QuantitaPasto> quantitaPasto = new ArrayList<>();

    //endregion

    //region Metodi

    public Menu() { }

    public Menu(Date dataInizio, int ricorrenza) {
        DataInizio = dataInizio;
        Ricorrenza = ricorrenza;
    }

    public int getID() {
        return ID;
    }

    public Date getDataInizio() {
        return DataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        DataInizio = dataInizio;
    }

    public int getRicorrenza() {
        return Ricorrenza;
    }

    public void setRicorrenza(int ricorrenza) {
        Ricorrenza = ricorrenza;
    }

    public List<QuantitaPasto> getQuantitaPasto()
    {
        List<QuantitaPasto> ritorno = new ArrayList<>();
        Collections.copy(ritorno, quantitaPasto);
        Collections.unmodifiableList(ritorno);
        return ritorno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return getID() == menu.getID() &&
                getRicorrenza() == menu.getRicorrenza() &&
                getDataInizio().compareTo(menu.getDataInizio()) == 0;
    }

    //endregion
}
