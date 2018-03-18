package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "QuantitaPasti")
public class QuantitaPasto implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

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

}
