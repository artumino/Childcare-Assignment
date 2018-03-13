package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.persistence.*;
import java.io.Serializable;

@DatabaseTable(tableName = "Gruppo")
@Entity
@Table(name = "Gruppo")
public class Gruppo implements Serializable
{
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;
    //Qua completeremo con ID Tutore e ID Bambino esterni
}
