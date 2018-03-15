package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@DatabaseTable(tableName = "PianoViaggi")
@Entity
@Table(name = "PianoViaggi")
public class PianoViaggi implements Serializable
{
    //region Attributi

    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    //endregion

    //region Relazioni

    @OneToOne(optional = false, cascade = CascadeType.REMOVE)
    private Gita Destinazione;

    @OneToOne(optional = false, cascade = CascadeType.REMOVE)
    private Gruppo Gruppo;

    @OneToOne(optional = false, cascade = CascadeType.REMOVE)
    private MezzoDiTrasporto Mezzo;

    //endregion
}
