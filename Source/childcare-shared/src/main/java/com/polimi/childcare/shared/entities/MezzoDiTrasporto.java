package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@DatabaseTable(tableName = "MezzoDiTrasporto")
@Entity
@Table(name = "MezzoDiTrasporto")
public class MezzoDiTrasporto implements Serializable
{
    //region Attributi

    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 7)   //Targa Italiana
    private String Targa;

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    @Column(nullable = false)
    private int Capienza;

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    @Column(nullable = false)
    private int NumeroIdentificativo;

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    @Column(nullable = false)
    private int CostoOrario;

    //endregion Attributi

    //region Relazioni

    @OneToOne(optional = false, cascade = CascadeType.REFRESH)
    private Fornitore fornitore;

    //endregion
}
