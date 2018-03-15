package com.polimi.childcare.shared.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Genitori")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Genitore extends Persona
{
    //region Relazioni

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "TutoriLegali",
            joinColumns = { @JoinColumn(name = "Genitore_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Bambino_FK") }
    )
    private List<Bambino> bambini;

    //endregion
}
