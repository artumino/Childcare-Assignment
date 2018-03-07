package com.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "RegistroPresenze")
public class RegistroPresenze
{
    @DatabaseField(generatedId = true) private Long ID;
    @DatabaseField(foreign = true, foreignColumnName = "ID", canBeNull = false) private Persona Persona;
    @DatabaseField(dataType = DataType.ENUM_INTEGER, canBeNull = false) private StatoPresenza Stato;
    @DatabaseField(dataType = DataType.DATE, canBeNull = false) private java.util.Date Date;
    @DatabaseField(dataType = DataType.DATE_TIME, canBeNull = false) private java.util.Date TimeStamp;
    @DatabaseField(dataType = DataType.SHORT, canBeNull = false) private short Ora;

    public enum StatoPresenza
    {
        Presente(), Assente(), EntratoInRitardo(), UscitoInAnticipo(), Disperso();
    }
}
