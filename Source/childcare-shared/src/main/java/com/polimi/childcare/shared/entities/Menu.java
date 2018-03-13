package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "Menu")
@Entity
@Table(name = "Menu")
public class Menu implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    @DatabaseField(dataType = DataType.DATE, canBeNull = false)
    @Column(nullable = false)
    private Date DataInizio;

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    @Column(nullable = false)
    private int Ricorrenza;
}
