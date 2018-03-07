package com.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MezzoDiTrasporto")
public class MezzoDiTrasporto
{
    @DatabaseField(generatedId = true) private Long ID;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false)  private String Targa;
    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false) private int Capienza;
    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false) private int NumeroIdentificativo;
    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false) private int CostoOrario;
}
