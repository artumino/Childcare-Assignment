package com.polimi.childcare.shared.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@DatabaseTable(tableName = "Contatto")
@Entity
@Table(name = "Contatto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Pediatra", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "0")
public class Contatto implements Serializable
{
    //region Attributi

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

    //endregion

    //region Relazioni

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Riferimenti",
            joinColumns = { @JoinColumn(name = "contatto_id") },
            inverseJoinColumns = { @JoinColumn(name = "bambino_id") }
    )
    private List<Bambino> bambini;

    //endregion
}
