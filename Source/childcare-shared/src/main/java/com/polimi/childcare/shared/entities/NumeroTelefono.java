package com.polimi.childcare.shared.entities;
import javax.persistence.*;

@Entity
@Table(name = "Rubrica")
public class NumeroTelefono
{
    //region Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int ID;

    @Column(nullable = false, length = 15)   //Standard E.164
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

    //endregion
}
