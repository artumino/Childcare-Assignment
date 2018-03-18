package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RegistroPresenze")
public class RegistroPresenze implements Serializable
{
    //region Attributi
    @Id
    private Long ID;

    @Enumerated(EnumType.ORDINAL)
    private StatoPresenza Stato;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date Date;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date TimeStamp;

    @Column(nullable = false)
    private short Ora;

    //endregion

    //region Relazioni

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name="Bambino_FK")
    private Bambino bambino;

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "Gita_FK")
    private Gita gita;

    //endregion

    public enum StatoPresenza
    {
        Presente(), Assente(), EntratoInRitardo(), UscitoInAnticipo(), Disperso();
    }
}
