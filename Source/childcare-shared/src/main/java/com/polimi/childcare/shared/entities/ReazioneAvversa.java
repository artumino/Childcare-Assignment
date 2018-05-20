package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "ReazioniAvverse")
public class ReazioneAvversa implements Serializable, ITransferable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 20)
    private String Nome;

    @Column(length = 50)
    private String Descrizione;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "reazioneAvversa")
    private Set<Diagnosi> diagnosi = new HashSet<>();

    @ManyToMany(mappedBy = "reazione", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Pasto> pasti = new HashSet<>();

    //endregion

    //region Metodi

    public ReazioneAvversa() { }

    public ReazioneAvversa(String nome, String descrizione) {
        Nome = nome;
        Descrizione = descrizione;
    }

    public int getID() {
        return ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public Set<Diagnosi> getDiagnosi()
    {
        Set<Diagnosi> ritorno = new HashSet<>(diagnosi);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    public Set<Pasto> getPasti()
    {
        Set<Pasto> ritorno = new HashSet<>(pasti);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, ReazioneAvversa.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReazioneAvversa)) return false;
        ReazioneAvversa that = (ReazioneAvversa) o;
        return getID() == that.getID() &&
                getNome().equals(that.getNome()) &&
                getDescrizione().equals(that.getDescrizione());
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
        pasti = this.getPasti();
        diagnosi = this.getDiagnosi();

        //Aggiorna figli
        DTOUtils.iterableToDTO(pasti);
        DTOUtils.iterableToDTO(diagnosi);
    }

    @Override
    public boolean isDTO()
    {
        return (pasti instanceof HashSet) && (diagnosi instanceof HashSet);
    }

    //endregion
}
