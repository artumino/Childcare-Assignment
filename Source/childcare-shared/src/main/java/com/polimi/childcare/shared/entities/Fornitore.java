package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Fornitori")
public class Fornitore implements Serializable
{
    //region Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 50)
    private String RagioneSociale;

    @Column(nullable = false, length = 11)  //In Italia 11 cifre, problemi sono con Olanda e Svezia che sono a 12, ma abbiamo detto che deve essere Italiana e iscritta al Registro Imprese
    private String PartitaIVA;

    @Column(nullable = false, length = 50)
    private String SedeLegale;

    @Column(nullable = false, length = 15)  //Lascio a 15, non ho trovato un documento ufficiale che ne attesti la lunghezza
    private String NumeroRegistroImprese;

    @Column(length = 30)
    private String Email;

    @Column(length = 15)   //10 numeri + 3 di prefisso (oppure 4 se conti due 0 come il +), lascio a 15 per sicurezza
    private String FAX;

    @Column(length = 27)
    private String IBAN;    //Ancora caso Italiano 27 caratteri

    //Manca Numeri di Telefono (campo multiplo)
    //endregion

    //region Relazioni

    @ManyToMany(mappedBy = "fornitori")
    private List<Pasto> pasti;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fornitore")
    private List<MezzoDiTrasporto> mezzi;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Fornitore_Fax_Rubrica",
            joinColumns = { @JoinColumn(name = "Fornitore_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Rubrica_FK") }
    )
    private List<NumeroTelefono> fax;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Fornitore_Rubrica",
            joinColumns = { @JoinColumn(name = "Fornitore_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Rubrica_FK") }
    )
    private List<NumeroTelefono> telefoni;

    //endregion

    //region Metodi

    public Fornitore() { }

    public Fornitore(String ragioneSociale, String partitaIVA, String sedeLegale, String numeroRegistroImprese, String email, String FAX, String IBAN) {
        RagioneSociale = ragioneSociale;
        PartitaIVA = partitaIVA;
        SedeLegale = sedeLegale;
        NumeroRegistroImprese = numeroRegistroImprese;
        Email = email;
        this.FAX = FAX;
        this.IBAN = IBAN;
    }

    public int getID() {
        return ID;
    }

    public String getRagioneSociale() {
        return RagioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        RagioneSociale = ragioneSociale;
    }

    public String getPartitaIVA() {
        return PartitaIVA;
    }

    public void setPartitaIVA(String partitaIVA) {
        PartitaIVA = partitaIVA;
    }

    public String getSedeLegale() {
        return SedeLegale;
    }

    public void setSedeLegale(String sedeLegale) {
        SedeLegale = sedeLegale;
    }

    public String getNumeroRegistroImprese() {
        return NumeroRegistroImprese;
    }

    public void setNumeroRegistroImprese(String numeroRegistroImprese) { NumeroRegistroImprese = numeroRegistroImprese; }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFAX() {
        return FAX;
    }

    public void setFAX(String FAX) {
        this.FAX = FAX;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public List<Pasto> getPasti() { return pasti; }

    public List<MezzoDiTrasporto> getMezzi() { return mezzi; }

    public List<NumeroTelefono> getFax() { return fax; }

    public List<NumeroTelefono> getTelefoni() { return telefoni; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fornitore)) return false;
        Fornitore fornitore = (Fornitore) o;
        return getID() == fornitore.getID() &&
                getRagioneSociale() == fornitore.getRagioneSociale() &&
                getPartitaIVA() == fornitore.getPartitaIVA() &&
                getSedeLegale() == fornitore.getSedeLegale() &&
                getNumeroRegistroImprese() == fornitore.getNumeroRegistroImprese() &&
                getEmail() == fornitore.getEmail() &&
                getFAX() == fornitore.getFAX() &&
                getIBAN() ==  fornitore.getIBAN();
    }

    //endregion
}
