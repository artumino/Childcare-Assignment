package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Contatti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Pediatra", discriminatorType = DiscriminatorType.CHAR, length = 1)
@DiscriminatorValue(value = "0")
public class Contatto implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

    @Column(length = 45)   //Di default è nullable
    private String Descrizione;

    @Column(nullable = false, length = 25)
    private String Nome;

    @Column(nullable = false, length = 25)
    private String Cognome;

    @Column(nullable = false, length = 25)
    private String Indirizzo;

    //endregion

    //region Relazioni

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Riferimenti",
            joinColumns = { @JoinColumn(name = "Contatto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Bambino_FK") }
    )
    private List<Bambino> bambini;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Contatto_Rubrica",
            joinColumns = { @JoinColumn(name = "Contatto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Rubrica_FK") }
    )
    private List<NumeroTelefono> telefoni;

    //endregion
}
