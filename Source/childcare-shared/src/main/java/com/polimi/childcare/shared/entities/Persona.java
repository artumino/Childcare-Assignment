package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "persona")
    private List<Diagnosi> diagnosi;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Persona_Rubrica",
            joinColumns = { @JoinColumn(name = "Persona_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Rubrica_FK") }
    )
    private List<NumeroTelefono> telefoni;

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

    public void addDiagnosi(Diagnosi d)
    {
        if(diagnosi == null)
            diagnosi = new ArrayList<>();
        diagnosi.add(d);
    }   //Poi va fatto update del Database

    public void removeDiagnosi(Diagnosi d)
    {
        if(diagnosi != null)
            diagnosi.remove(d);
    }   //Poi va fatto update del Database

    public void addNumero(NumeroTelefono n)
    {
        if(telefoni == null)
            telefoni = new ArrayList<>();

        telefoni.add(n);
    }  //Poi va fatto update del Database

    public void removeNumero(NumeroTelefono n)
    {
        if(telefoni != null)
            telefoni.remove(n);
    }  //Poi va fatto update del Database

    public List<Diagnosi> getDiagnosi() { return diagnosi; }

    public List<NumeroTelefono> getTelefoni() { return telefoni; }

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
