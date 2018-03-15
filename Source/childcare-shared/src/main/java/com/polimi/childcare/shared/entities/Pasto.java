package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@DatabaseTable(tableName = "Pasto")
@Entity
@Table(name = "Pasto")
public class Pasto implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 20)
    private String Nome;

    @DatabaseField(dataType = DataType.STRING)
    @Column(length = 50)
    private String Descrizione;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Fornitore_Pasto",
            joinColumns = { @JoinColumn(name = "pasto_id") },
            inverseJoinColumns = { @JoinColumn(name = "fornitore_id") }
    )
    private List<Fornitore> Fornitori;
}
