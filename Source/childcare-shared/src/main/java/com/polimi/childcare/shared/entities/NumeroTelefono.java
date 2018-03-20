package com.polimi.childcare.shared.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Rubrica")
public class NumeroTelefono
{
    //region Attributi
    @Id
    private Long ID;

    @Column(nullable = false, length = 15)   //Standard E.164
    private String Numero;

    //endregion

    //region Metodi

    public NumeroTelefono() { }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    //endregion
}
