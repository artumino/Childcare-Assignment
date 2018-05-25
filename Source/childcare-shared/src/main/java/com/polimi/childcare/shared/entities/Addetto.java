package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Addetti")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Addetto extends Persona
{
    //region Metodi

    public Addetto() { }

    public Addetto(String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, byte sesso) {
        super(nome, cognome, codiceFiscale, dataNascita, stato, comune, provincia, cittadinanza, residenza, sesso);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion
}
