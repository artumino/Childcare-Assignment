package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@DatabaseTable(tableName = "Diagnosi")
@Entity
@Table(name = "Diagnosi")
public class Diagnosi implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    @OneToMany(mappedBy = "persone")
    public List<Persona> Persona;

    @OneToMany(mappedBy = "reazioni")
    public List<ReazioniAvverse> ReazioniAvverse;

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    @Column(nullable = false)
    private int Allergia;

}
