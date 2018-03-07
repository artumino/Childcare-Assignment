package com.childcare.client;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Menu")
public class Menu
{
    @DatabaseField(generatedId = true) private Long ID;
    @DatabaseField(dataType = DataType.DATE, canBeNull = false)    private Date DataInizio;
    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false) private int Ricorrenza;
}
