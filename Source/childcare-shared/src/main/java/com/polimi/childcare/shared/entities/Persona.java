package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import java.util.Date;

@DatabaseTable(tableName = "Persona")
@DiscriminatorColumn(name = "TipoPersona", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Persona
{
    @DatabaseField(generatedId = true) protected Long ID;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String Nome;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String Cognome;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String CodiceFiscale;
    @DatabaseField(dataType = DataType.DATE, canBeNull = false)   protected Date DataNascita;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String Stato;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String Comune;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String Provincia;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String Cittadinanza;
    @DatabaseField(dataType = DataType.STRING, canBeNull = false) protected String Residenza;
    @DatabaseField(dataType = DataType.BYTE, canBeNull = false)   protected byte Sesso;

    //TODO: Gestire l'istanziazione (Decorator??)
    public enum TipoPersona
    {
        Bambino(), Genitore(), Addetto();
    }
}
