package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "QuantitaPasti")
public class QuantitaPasto implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    private int Quantita;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Pasto_FK")
    private Pasto pasto;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Menu_FK")
    private Menu menu;

    //endregion

    //region Metodi

    public QuantitaPasto() { }

    public QuantitaPasto(int quantita, Pasto pasto, Menu menu) {
        Quantita = quantita;
        this.pasto = pasto;
        this.menu = menu;
    }

    public int getID() {
        return ID;
    }

    public int getQuantita() {
        return Quantita;
    }

    public void setQuantita(int quantita) {
        Quantita = quantita;
    }

    public Pasto getPasto() {
        return pasto;
    }

    public void setPasto(Pasto pasto) {
        this.pasto = pasto;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuantitaPasto)) return false;
        QuantitaPasto that = (QuantitaPasto) o;
        return getID() == that.getID() &&
                getQuantita() == that.getQuantita(); //&&
                //getPasto().equals(that.getPasto()) &&
                //getMenu().equals(that.getMenu());
    }

    //endregion

}
