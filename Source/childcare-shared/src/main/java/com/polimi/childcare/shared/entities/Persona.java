package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Persone")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona extends TransferableEntity implements Serializable
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
    protected LocalDate DataNascita;

    @Column(nullable = false, length = 20)
    protected String Stato;

    @Column(nullable = false, length = 45)
    protected String Comune;

    @Column(nullable = false, length = 45)
    protected String Provincia;

    @Column(nullable = false, length = 20)
    protected String Cittadinanza;

    @Column(nullable = false, length = 50)
    protected String Residenza;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    protected ESesso Sesso;

    @Column()
    private String telefoni;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "persona")
    private Set<Diagnosi> diagnosi = new HashSet<>();


    //endregion

    //region Metodi

    public Persona() { }

    public Persona(String nome, String cognome, String codiceFiscale, LocalDate dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, ESesso sesso)
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

    public LocalDate getDataNascita() {
        return DataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
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

    public ESesso getSesso() {
        return Sesso;
    }

    public void setSesso(ESesso sesso) {
        Sesso = sesso;
    }

    public List<String> getTelefoni() { return EntitiesHelper.getNumeriTelefonoFromString(telefoni); }

    public void setTelefoni(List<String> telefoni) { this.telefoni = EntitiesHelper.getTelefoniStringFromIterable(telefoni); }

    public void addTelefono(String telefono) { telefoni = EntitiesHelper.addTelefonoToString(telefoni, telefono); }

    public void removeTelefono(String telefono) { telefoni = EntitiesHelper.removeTelefonoToString(telefoni, telefono); }

    public void unsafeAddDiagnosi(Diagnosi d) { diagnosi.add(d); }

    public void unsafeRemoveDiagnosi(Diagnosi d) { diagnosi.remove(d); }

    public Set<Diagnosi> getDiagnosi() { return EntitiesHelper.unmodifiableListReturn(diagnosi); }

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
                getDataNascita().isEqual(persona.getDataNascita()) &&
                getStato().equals(persona.getStato()) &&
                getComune().equals(persona.getComune()) &&
                getProvincia().equals(persona.getProvincia()) &&
                getCittadinanza().equals(persona.getCittadinanza()) &&
                getResidenza().equals(persona.getResidenza());
    }

    //endregion

    //region Enumeratori

    public enum ESesso
    {
        Maschio(), Femmina(), Altro()
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
        diagnosi = DTOUtils.iterableToDTO(diagnosi, processed);

        //Trasformo in Set
        diagnosi = this.getDiagnosi();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(diagnosi);
    }

    //endregion

    //region Relations Interfaces

    public IOneToMany<Diagnosi, Persona> asPersonaDiagnosiRelation()
    {
        return new IOneToMany<Diagnosi, Persona>() {
            @Override
            public Persona getItem() {
                return Persona.this;
            }

            @Override
            public void unsafeAddRelation(Diagnosi item) {
                unsafeAddDiagnosi(item);
            }

            @Override
            public void unsafeRemoveRelation(Diagnosi item) {
                unsafeRemoveDiagnosi(item);
            }

            @Override
            public Set<Diagnosi> getUnmodifiableRelation() {
                return getDiagnosi();
            }

            @Override
            public IManyToOne<Persona, Diagnosi> getInverse(Diagnosi item) {
                return item.asDiagnosiPersonaRelation();
            }
        };
    }


    //endregion
}
