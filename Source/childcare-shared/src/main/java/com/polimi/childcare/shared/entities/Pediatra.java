package com.polimi.childcare.shared.entities;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "1")
public class Pediatra extends Contatto
{
}
