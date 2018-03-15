package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ReazioniAvverse")
public class ReazioniAvverse implements Serializable
{
    //region Attributi

    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    private String Nome;

    @DatabaseField(dataType = DataType.STRING)
    @Column(length = 50)
    private String Descrizione;

    //endregion

    //region Relazioni

    //endregion
}
