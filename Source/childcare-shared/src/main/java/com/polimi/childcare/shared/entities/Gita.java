package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Gite")
public class Gita implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date DataInizio;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date DataFine;

    @Column(nullable = false, length = 50)
    private String Luogo;

    @Column(nullable = false)
    private int Costo;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gita")
    private List<PianoViaggi> pianiViaggi;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gita")
    private List<RegistroPresenze> registriPresenze;

    //endregion
}
