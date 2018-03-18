package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ReazioniAvverse")
public class ReazioneAvversa implements Serializable
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reazioneAvversa")
    private List<Diagnosi> diagnosi;

    @ManyToMany(mappedBy = "reazione")
    private List<Pasto> pasti;

    //endregion
}
