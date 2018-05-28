package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwned;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwner;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Contatti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Pediatra", discriminatorType = DiscriminatorType.CHAR, length = 1)
@DiscriminatorValue(value = "0")
public class Contatto extends TransferableEntity implements Serializable
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

    @Column(nullable = false, length = 75)
    private String Indirizzo;

    @Column
    private String telefoni;

    //endregion

    //region Relazioni

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Riferimenti",
            joinColumns = { @JoinColumn(name = "Contatto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Bambino_FK") }
    )
    private Set<Bambino> bambini = new HashSet<>();


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

    public List<String> getTelefoni() { return EntitiesHelper.getNumeriTelefonoFromString(telefoni); }

    public void setTelefoni(List<String> telefoni) { this.telefoni = EntitiesHelper.getTelefoniStringFromIterable(telefoni); }

    public void addTelefono(String telefono) { telefoni = EntitiesHelper.addTelefonoToString(telefoni, telefono); }

    public void removeTelefono(String telefono) { telefoni = EntitiesHelper.removeTelefonoToString(telefoni, telefono); }

    public void addBambino(Bambino b) { bambini.add(b); }

    public void removeBambino(Bambino b) { bambini.remove(b); }

    public Set<Bambino> getBambini() { return EntitiesHelper.unmodifiableListReturn(bambini); }

    @Override
    public int hashCode() { return Objects.hash(ID, Contatto.class); }

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

    //region DTO


    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO(List<Object> processed)
    {
        bambini = DTOUtils.iterableToDTO(bambini, processed);

        bambini = getBambini();
    }

    @Override
    public boolean isDTO() {
        return DTOUtils.isDTO(bambini);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    //region Relations Interfaces

    public IManyToManyOwner<Bambino, Contatto> asContattiBambiniRelation()
    {
        return new IManyToManyOwner<Bambino, Contatto>() {

            @Override
            public Contatto getItem()
            {
                return Contatto.this;
            }

            @Override
            public void addRelation(Bambino item)
            {
                addBambino(item);
            }

            @Override
            public void removeRelation(Bambino item)
            {
                removeBambino(item);
            }

            @Override
            public Set<Bambino> getUnmodifiableRelation()
            {
                return getBambini();
            }

            @Override
            public IManyToManyOwned<Contatto, Bambino> getInverse(Bambino item)
            {
                return item.asBambiniContattiRelation();
            }
        };
    }

    //endregion
}
