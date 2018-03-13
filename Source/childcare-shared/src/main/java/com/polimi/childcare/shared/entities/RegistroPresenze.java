package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "RegistroPresenze")
@Entity
@Table(name = "RegistroPresenza")
public class RegistroPresenze implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    @DatabaseField(foreign = true, foreignColumnName = "ID", canBeNull = false) //TODO: Foreing in hibernate
    private Persona Persona;

    @DatabaseField(dataType = DataType.ENUM_INTEGER, canBeNull = false) //TODO: Enum in hibernate
    private StatoPresenza Stato;

    @DatabaseField(dataType = DataType.DATE, canBeNull = false)
    @Column(nullable = false)
    private Date Date;

    @DatabaseField(dataType = DataType.DATE_TIME, canBeNull = false)
    @Column(nullable = false)
    private Date TimeStamp;

    @DatabaseField(dataType = DataType.SHORT, canBeNull = false)
    @Column(nullable = false)
    private short Ora;

    public enum StatoPresenza
    {
        Presente(), Assente(), EntratoInRitardo(), UscitoInAnticipo(), Disperso();
    }
}
