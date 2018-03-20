package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Menu")
public class Menu implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date DataInizio;

    @Column(nullable = false)
    private int Ricorrenza;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "menu")
    private List<QuantitaPasto> quantitaPasto;

    //endregion

    //region Metodi

    public Menu() { }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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

    //endregion
}
