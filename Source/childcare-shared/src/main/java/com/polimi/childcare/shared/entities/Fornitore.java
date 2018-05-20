package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Fornitori")
public class Fornitore implements Serializable, ITransferable
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

    @Column(length = 27)
    private String IBAN;    //Ancora caso Italiano 27 caratteri

    //Manca Numeri di Telefono (campo multiplo)
    //endregion

    //region Relazioni

    @ManyToMany(mappedBy = "fornitori", fetch = FetchType.LAZY)
    private Set<Pasto> pasti = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "fornitore")
    private Set<MezzoDiTrasporto> mezzi = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Fornitore_Fax_Rubrica",
            joinColumns = { @JoinColumn(name = "Fornitore_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Rubrica_FK") }
    )
    private Set<NumeroTelefono> fax = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Fornitore_Rubrica",
            joinColumns = { @JoinColumn(name = "Fornitore_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Rubrica_FK") }
    )
    private Set<NumeroTelefono> telefoni = new HashSet<>();

    //endregion

    //region Metodi

    public Fornitore() { }

    public Fornitore(String ragioneSociale, String partitaIVA, String sedeLegale, String numeroRegistroImprese, String email, String IBAN) {
        RagioneSociale = ragioneSociale;
        PartitaIVA = partitaIVA;
        SedeLegale = sedeLegale;
        NumeroRegistroImprese = numeroRegistroImprese;
        Email = email;
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

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void addFax(NumeroTelefono f) { fax.add(f); }

    public void removeFax(NumeroTelefono f) { fax.remove(f); }

    public void addTelefono(NumeroTelefono t) { telefoni.add(t); }

    public void removeTelefono(NumeroTelefono t) { telefoni.remove(t); }

    public Set<Pasto> getPasti() { return EntitiesHelper.unmodifiableListReturn(pasti); }

    public Set<MezzoDiTrasporto> getMezzi() { return EntitiesHelper.unmodifiableListReturn(mezzi); }

    public Set<NumeroTelefono> getFax() { return EntitiesHelper.unmodifiableListReturn(fax); }

    public Set<NumeroTelefono> getTelefoni() { return EntitiesHelper.unmodifiableListReturn(telefoni); }

    @Override
    public int hashCode() { return Objects.hash(ID, Fornitore.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fornitore)) return false;
        Fornitore fornitore = (Fornitore) o;
        return getID() == fornitore.getID() &&
                getRagioneSociale().equals(fornitore.getRagioneSociale()) &&
                getPartitaIVA().equals(fornitore.getPartitaIVA()) &&
                getSedeLegale().equals(fornitore.getSedeLegale()) &&
                getNumeroRegistroImprese().equals(fornitore.getNumeroRegistroImprese()) &&
                getEmail().equals(fornitore.getEmail()) &&
                getIBAN().equals(fornitore.getIBAN());
    }

    //endregion

    //region DTO


    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO()
    {
        fax = this.getFax();
        telefoni = this.getTelefoni();
        pasti = this.getPasti();
        mezzi = this.getMezzi();

        //Aggiorna figli
        DTOUtils.iterableToDTO(pasti);
        DTOUtils.iterableToDTO(mezzi);
    }

    @Override
    public boolean isDTO()
    {
        return (pasti instanceof HashSet) && (mezzi instanceof HashSet) && (fax instanceof HashSet) && (telefoni instanceof HashSet);
    }

    //endregion
}
