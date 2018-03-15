package com.polimi.childcare.shared.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Addetti")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Addetto extends Persona
{
}
