package com.polimi.childcare.shared.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "0")
public class Bambino extends Persona
{

}
