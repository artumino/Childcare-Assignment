package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "QuantitaPasti")
public class QuantitaPasto extends TransferableEntity implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    private int Quantita;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "Pasto_FK")
    private Pasto pasto;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
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
    public int hashCode() { return Objects.hash(ID, QuantitaPasto.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuantitaPasto)) return false;
        QuantitaPasto that = (QuantitaPasto) o;
        return getID() == that.getID() &&
                getQuantita() == that.getQuantita();
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
        menu = DTOUtils.objectToDTO(menu, processed);
        pasto = DTOUtils.objectToDTO(pasto, processed);
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(menu) && DTOUtils.isDTO(pasto);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion


    //region Relations Interfaces

    public IManyToOne<Pasto, QuantitaPasto> asQuantitaPastiPastoRelation()
    {
        return new IManyToOne<Pasto, QuantitaPasto>() {
            @Override
            public QuantitaPasto getItem() {
                return QuantitaPasto.this;
            }

            @Override
            public void setRelation(Pasto item) {
                setPasto(item);
            }

            @Override
            public Pasto getRelation() {
                return getPasto();
            }

            @Override
            public IOneToMany<QuantitaPasto, Pasto> getInverse(Pasto item) {
                return item.asPastoQuantitaPastiRelation();
            }
        };
    }

    public IManyToOne<Menu, QuantitaPasto> asQuantitaPastiMenuRelation()
    {
        return new IManyToOne<Menu, QuantitaPasto>() {
            @Override
            public QuantitaPasto getItem() {
                return QuantitaPasto.this;
            }

            @Override
            public void setRelation(Menu item) {
                setMenu(item);
            }

            @Override
            public Menu getRelation() {
                return getMenu();
            }

            @Override
            public IOneToMany<QuantitaPasto, Menu> getInverse(Menu item) {
                return item.asMenuQuantitaPastiRelation();
            }
        };

    }

    //endregion
}
