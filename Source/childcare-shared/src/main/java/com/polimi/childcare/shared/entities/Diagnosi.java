package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Diagnosi")
public class Diagnosi implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

    @Column(nullable = false)
    private boolean Allergia;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Persona_FK")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "ReazioneAvversa_FK")
    private ReazioneAvversa reazioneAvversa;

    //endregion

}
