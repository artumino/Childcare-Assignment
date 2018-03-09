package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Gruppo")
public class Gruppo
{
    @DatabaseField(generatedId = true) private Long ID;
    //Qua completeremo con ID Tutore e ID Bambino esterni
}
