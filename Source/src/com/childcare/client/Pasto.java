package com.childcare.client;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Pasto")
public class Pasto
{
    @DatabaseField(generatedId = true) private Long ID;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String Nome;
    @DatabaseField(dataType = DataType.STRING)                    private String Descrizione;
}
