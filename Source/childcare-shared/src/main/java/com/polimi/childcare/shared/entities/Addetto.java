package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Addetti")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Addetto extends Persona
{
}
