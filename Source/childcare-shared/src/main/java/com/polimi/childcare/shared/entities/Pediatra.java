package com.polimi.childcare.shared.entities;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
public class Pediatra extends Contatto
{
}
