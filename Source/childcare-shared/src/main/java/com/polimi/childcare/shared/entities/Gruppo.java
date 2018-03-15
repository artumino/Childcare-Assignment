package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@DatabaseTable(tableName = "Gruppo")
@Entity
@Table(name = "Gruppo")
public class Gruppo implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;
    //Qua completeremo con ID Tutore e ID Bambino esterni

    @OneToOne(optional = false, cascade = CascadeType.REFRESH)
    private Addetto sorvergliante;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Gruppo_Bambino",
            joinColumns = { @JoinColumn(name = "gruppo_id") },
            inverseJoinColumns = { @JoinColumn(name = "bambino_id") }
    )
    private Set<Bambino> bambini;
}
