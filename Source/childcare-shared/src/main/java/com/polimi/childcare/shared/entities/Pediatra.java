package com.polimi.childcare.shared.entities;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue(value = "1")
public class Pediatra extends Contatto
{
    //Relazioni
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pediatra")
    private List<Bambino> bambini;
}
