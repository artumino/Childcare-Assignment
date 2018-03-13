package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "Persona")
@Entity
@Table(name = "Persona")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TipoPersona", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Persona implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    protected Long ID;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    protected String Nome;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    protected String Cognome;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 16)  //C.F. Italiano 16 caratteri
    protected String CodiceFiscale;

    @DatabaseField(dataType = DataType.DATE, canBeNull = false)
    @Column(nullable = false)
    protected Date DataNascita;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    protected String Stato;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    protected String Comune;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    protected String Provincia;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    protected String Cittadinanza;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 50)
    protected String Residenza;

    @DatabaseField(dataType = DataType.BYTE, canBeNull = false)
    @Column(nullable = false)
    protected byte Sesso;

    public enum TipoPersona
    {
        Bambino(), Genitore(), Addetto();

        public int getOrdinal()
        {
            return ordinal();
        }
    }
}
