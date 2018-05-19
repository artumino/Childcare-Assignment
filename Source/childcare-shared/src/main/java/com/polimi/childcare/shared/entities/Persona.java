package com.polimi.childcare.shared.entities;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Persone")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona implements Serializable
{
    //region Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int ID;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "persona")
    private Set<Diagnosi> diagnosi = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Persona_Rubrica",
            joinColumns = { @JoinColumn(name = "Persona_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Rubrica_FK") }
    )
    private Set<NumeroTelefono> telefoni = new HashSet<>();

    //endregion

    //region Metodi

    public Persona() { }

    public Persona(String nome, String cognome, String codiceFiscale, Date dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, byte sesso)
    {
        Nome = nome;
        Cognome = cognome;
        CodiceFiscale = codiceFiscale;
        DataNascita = dataNascita;
        Stato = stato;
        Comune = comune;
        Provincia = provincia;
        Cittadinanza = cittadinanza;
        Residenza = residenza;
        Sesso = sesso;
    }

    public int getID() { return ID; }

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

    public String getCodiceFiscale() {
        return CodiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        CodiceFiscale = codiceFiscale;
    }

    public Date getDataNascita() {
        return DataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        DataNascita = dataNascita;
    }

    public String getStato() {
        return Stato;
    }

    public void setStato(String stato) {
        Stato = stato;
    }

    public String getComune() {
        return Comune;
    }

    public void setComune(String comune) {
        Comune = comune;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getCittadinanza() {
        return Cittadinanza;
    }

    public void setCittadinanza(String cittadinanza) {
        Cittadinanza = cittadinanza;
    }

    public String getResidenza() {
        return Residenza;
    }

    public void setResidenza(String residenza) {
        Residenza = residenza;
    }

    public byte getSesso() {
        return Sesso;
    }

    public void setSesso(byte sesso) {
        Sesso = sesso;
    }

    public void addTelefono(NumeroTelefono n) { telefoni.add(n); }

    public void removeTelefono(NumeroTelefono n) { telefoni.remove(n); }

    public Set<Diagnosi> getDiagnosi()
    {
        Set<Diagnosi> ritorno = new HashSet<>(diagnosi);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    public Set<NumeroTelefono> getTelefoni()
    {
        Set<NumeroTelefono> ritorno = new HashSet<>(telefoni);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, Persona.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
        Persona persona = (Persona) o;
        return getID() == persona.getID() &&
                getSesso() == persona.getSesso()&&
                getNome().equals(persona.getNome()) &&
                getCognome().equals(persona.getCognome()) &&
                getCodiceFiscale().equals(persona.getCodiceFiscale()) &&
                getDataNascita().compareTo(persona.getDataNascita())  == 0 &&
                getStato().equals(persona.getStato()) &&
                getComune().equals(persona.getComune()) &&
                getProvincia().equals(persona.getProvincia()) &&
                getCittadinanza().equals(persona.getCittadinanza()) &&
                getResidenza().equals(persona.getResidenza());
    }

    //endregion
}
