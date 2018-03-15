package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "PianiViaggi")
public class PianoViaggi implements Serializable
{
    //region Attributi

    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Gita_FK")
    private Gita gita;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Gruppo_FK")
    private Gruppo gruppo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Mezzo_FK")
    private MezzoDiTrasporto mezzo;

    //endregion
}
