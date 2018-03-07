package com.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Contatto")
public class Contatto
{
    @DatabaseField(generatedId = true) private Long ID;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String Telefono;
    @DatabaseField(dataType = DataType.STRING) private String Descrizione;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String Nome;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String Cognome;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String Indirizzo;
    @DatabaseField(dataType = DataType.BOOLEAN, canBeNull = false) private boolean Pediatra;
}
