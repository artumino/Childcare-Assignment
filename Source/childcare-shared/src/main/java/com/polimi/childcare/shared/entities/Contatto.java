package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Contatti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Pediatra", discriminatorType = DiscriminatorType.CHAR, length = 1)
@DiscriminatorValue(value = "0")
public class Contatto implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

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

    //region Metodi

    public Contatto() { }

    public Contatto(String descrizione, String nome, String cognome, String indirizzo) {
        Descrizione = descrizione;
        Nome = nome;
        Cognome = cognome;
        Indirizzo = indirizzo;
    }

    public int getID() { return ID; }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        Indirizzo = indirizzo;
    }

    public void setBambini(List<Bambino> bambini) { this.bambini = bambini; }

    public void setTelefoni(List<NumeroTelefono> telefoni) { this.telefoni = telefoni; }

    public List<Bambino> getBambini() { return bambini; }

    public List<NumeroTelefono> getTelefoni() { return telefoni; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contatto)) return false;
        Contatto contatto = (Contatto) o;
        return getID() == contatto.getID() &&
                getDescrizione().equals(contatto.getDescrizione()) &&
                getNome().equals(contatto.getNome()) &&
                getCognome().equals(contatto.getCognome()) &&
                getIndirizzo().equals(contatto.getIndirizzo());
    }

    //endregion
}
