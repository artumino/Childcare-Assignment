package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Rubrica", indexes = {
        @Index(columnList = "Numero", name = "idx_Numero")})
public class NumeroTelefono implements Serializable
{
    //region Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 15, unique = true)   //Standard E.164
    private String Numero;

    //endregion

    //region Metodi

    public NumeroTelefono() { }

    public NumeroTelefono(String numero) {
        Numero = numero;
    }

    public int getID() {
        return ID;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, NumeroTelefono.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumeroTelefono)) return false;
        NumeroTelefono that = (NumeroTelefono) o;
        return getID() == that.getID() &&
                getNumero().equals(that.getNumero());
    }

    //endregion
}
