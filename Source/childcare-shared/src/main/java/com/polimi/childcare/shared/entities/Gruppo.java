package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Gruppi")
public class Gruppo implements Serializable
{
    //region Attributi

    @Id
    private Long ID;
    //Qua completeremo con ID Tutore e ID Bambino esterni

    //endregion

    //region Relazioni
    @OneToOne(optional = false, cascade = CascadeType.REFRESH)
    private Addetto sorvergliante;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Gruppo_Bambino",
            joinColumns = { @JoinColumn(name = "Gruppo_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Bambino_FK") }
    )
    private List<Bambino> bambini;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gruppo")
    private List<PianoViaggi> pianoviaggi;

    //endregion
}
