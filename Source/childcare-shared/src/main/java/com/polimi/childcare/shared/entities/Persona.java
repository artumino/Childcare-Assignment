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

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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

    public List<Diagnosi> getDiagnosi() {
        return diagnosi;
    }

    public void setDiagnosi(List<Diagnosi> diagnosi) {
        this.diagnosi = diagnosi;
    }

    public List<NumeroTelefono> getTelefoni() {
        return telefoni;
    }

    public void setTelefoni(List<NumeroTelefono> telefoni) {
        this.telefoni = telefoni;
    }

    //endregion
}
