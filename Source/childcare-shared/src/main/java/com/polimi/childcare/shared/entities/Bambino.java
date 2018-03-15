package com.polimi.childcare.shared.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@DiscriminatorValue(value = "0")
public class Bambino extends Persona
{
    @ManyToMany(mappedBy = "bambini")
    private Set<Gruppo> gruppi;
}
