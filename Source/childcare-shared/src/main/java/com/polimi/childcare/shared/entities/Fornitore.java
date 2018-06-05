package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Fornitori")
public class Fornitore extends TransferableEntity implements Serializable
{
    //region Attributi
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 50)
    private String RagioneSociale;

    @Column(nullable = false, length = 11)  //In Italia 11 cifre, problemi sono con Olanda e Svezia che sono a 12, ma abbiamo detto che deve essere Italiana e iscritta al Registro Imprese
    private String PartitaIVA;

    @Column(nullable = false, length = 125)
    private String SedeLegale;

    @Column(nullable = false, length = 15)  //Lascio a 15, non ho trovato un documento ufficiale che ne attesti la lunghezza
    private String NumeroRegistroImprese;

    @Column(length = 30)
    private String Email;

    @Column(length = 27)
    private String IBAN;    //Ancora caso Italiano 27 caratteri

    @Column
    private String telefoni;

    @Column
    private String fax;

    //Manca Numeri di Telefono (campo multiplo)
    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fornitore")
    private Set<Pasto> pasti = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fornitore")
    private Set<MezzoDiTrasporto> mezzi = new HashSet<>();

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

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
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

    public List<String> getFax() { return EntitiesHelper.getNumeriTelefonoFromString(fax); }

    public void setFax(List<String> fax) { this.fax = EntitiesHelper.getTelefoniStringFromIterable(fax); }

    public void addFax(String fax) { this.fax = EntitiesHelper.addTelefonoToString(this.fax, fax); }

    public void removeFax(String fax) { this.fax = EntitiesHelper.removeTelefonoToString(this.fax, fax); }

    public List<String> getTelefoni() { return EntitiesHelper.getNumeriTelefonoFromString(telefoni); }

    public void setTelefoni(List<String> telefoni) { this.telefoni = EntitiesHelper.getTelefoniStringFromIterable(telefoni); }

    public void addTelefono(String telefono) { telefoni = EntitiesHelper.addTelefonoToString(telefoni, telefono); }

    public void removeTelefono(String telefono) { telefoni = EntitiesHelper.removeTelefonoToString(telefoni, telefono); }

    public void unsafeAddPasto(Pasto p) { pasti.add(p); }

    public void unsafeRemovePasto(Pasto p) { pasti.remove(p); }

    public void unsafeAddMezzo(MezzoDiTrasporto m) { mezzi.add(m); }

    public void unsafeRemoveMezzo(MezzoDiTrasporto m) { mezzi.remove(m); }

    public Set<Pasto> getPasti() { return EntitiesHelper.unmodifiableListReturn(pasti); }

    public Set<MezzoDiTrasporto> getMezzi() { return EntitiesHelper.unmodifiableListReturn(mezzi); }

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
    public void toDTO(List<Object> processed)
    {
        //Aggiorna figli
        pasti = DTOUtils.iterableToDTO(pasti, processed);
        mezzi = DTOUtils.iterableToDTO(mezzi, processed);

        //Trasforma in non modificabili
        pasti = this.getPasti();
        mezzi = this.getMezzi();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(pasti) && DTOUtils.isDTO(mezzi);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    //region Relations Interfaces

    public IOneToMany<MezzoDiTrasporto, Fornitore> asFornitoreMezziDiTrasportoRelation()
    {
        return new IOneToMany<MezzoDiTrasporto, Fornitore>() {
            @Override
            public Fornitore getItem() {
                return Fornitore.this;
            }

            @Override
            public void unsafeAddRelation(MezzoDiTrasporto item) {
                unsafeAddMezzo(item);
            }

            @Override
            public void unsafeRemoveRelation(MezzoDiTrasporto item) {
                unsafeRemoveMezzo(item);
            }

            @Override
            public Set<MezzoDiTrasporto> getUnmodifiableRelation() {
                return getMezzi();
            }

            @Override
            public IManyToOne<Fornitore, MezzoDiTrasporto> getInverse(MezzoDiTrasporto item) {
                return item.asMezziDiTrasportoFornitoreRelation();
            }
        };
    }

    public IOneToMany<Pasto, Fornitore> asFornitorePastiRelation()
    {
        return new IOneToMany<Pasto, Fornitore>() {
            @Override
            public Fornitore getItem() {
                return Fornitore.this;
            }

            @Override
            public void unsafeAddRelation(Pasto item) {
                unsafeAddPasto(item);
            }

            @Override
            public void unsafeRemoveRelation(Pasto item) {
                unsafeRemovePasto(item);
            }

            @Override
            public Set<Pasto> getUnmodifiableRelation() {
                return getPasti();
            }

            @Override
            public IManyToOne<Fornitore, Pasto> getInverse(Pasto item) {
                return item.asPastoFornitoreRelation();
            }
        };
    }

    //endregion
}
