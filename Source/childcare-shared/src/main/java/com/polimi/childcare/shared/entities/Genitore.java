package com.polimi.childcare.shared.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
public class Genitore extends Persona
{
}
