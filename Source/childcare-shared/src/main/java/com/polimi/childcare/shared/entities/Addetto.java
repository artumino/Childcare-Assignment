package com.polimi.childcare.shared.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "2")
public class Addetto extends Persona
{
}
