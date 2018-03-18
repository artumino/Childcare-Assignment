package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Pasti")
public class Pasto implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

    @Column(nullable = false, length = 20)
    private String Nome;

    @Column(length = 50)
    private String Descrizione;

    //endregion

    //region Relazioni

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Fornitore_Pasto",
            joinColumns = { @JoinColumn(name = "Pasto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Fornitore_FK") }
    )
    private List<Fornitore> fornitori;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ReazioneAvversa_Pasto",
            joinColumns = { @JoinColumn(name = "Pasto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "ReazioneAvversa_FK") }
    )
    private List<ReazioneAvversa> reazione;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pasto")
    private List<QuantitaPasto> quantitaPasto;

    //endregion
}
