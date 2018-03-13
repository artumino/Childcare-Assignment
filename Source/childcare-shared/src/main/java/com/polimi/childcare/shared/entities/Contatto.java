package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;

@DatabaseTable(tableName = "Contatto")
@Entity
@Table(name = "Contatto")
@MappedSuperclass()
public class Contatto implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;


    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 15)
    private String Telefono;

    @DatabaseField(dataType = DataType.STRING)
    @Column(length = 45)   //Di default è nullable
    private String Descrizione;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 25)
    private String Nome;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 25)
    private String Cognome;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 25)
    private String Indirizzo;
}
