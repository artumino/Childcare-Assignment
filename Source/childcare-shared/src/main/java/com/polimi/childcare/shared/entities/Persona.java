package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Persone")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona implements Serializable
{
    //region Attributi
    @Id
    protected Long ID;

    @Column(nullable = false, length = 20)
    protected String Nome;

    @Column(nullable = false, length = 20)
    protected String Cognome;

    @Column(nullable = false, length = 16)  //C.F. Italiano 16 caratteri
    protected String CodiceFiscale;

    @Column(nullable = false)
    protected Date DataNascita;

    @Column(nullable = false, length = 20)
    protected String Stato;

    @Column(nullable = false, length = 20)
    protected String Comune;

    @Column(nullable = false, length = 20)
    protected String Provincia;

    @Column(nullable = false, length = 20)
    protected String Cittadinanza;

    @Column(nullable = false, length = 50)
    protected String Residenza;

    @Column(nullable = false)
    protected byte Sesso;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "persona")
    private List<Diagnosi> diagnosi;

    //endregion
}
