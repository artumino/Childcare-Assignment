package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ReazioniAvverse")
public class ReazioniAvverse
{
    @DatabaseField(generatedId = true) private Long ID;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String Nome;
    @DatabaseField(dataType = DataType.STRING)                    private String Descrizione;
}
