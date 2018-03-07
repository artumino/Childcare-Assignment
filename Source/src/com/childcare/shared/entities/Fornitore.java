package com.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Fornitore")
public class Fornitore
{
    @DatabaseField(generatedId = true) private Long ID;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String RagioneSociale;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String PartitaIVA;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String SedeLegale;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) private String NumeroRegistroImprese;
    @DatabaseField(dataType = DataType.STRING)                    private String Email;
    @DatabaseField(dataType = DataType.STRING)                    private String FAX;
    @DatabaseField(dataType = DataType.STRING)                    private String IBAN;

    //Mancano Numeri di Telefono e Prodotti (campi multipli)
}
