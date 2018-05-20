package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "ReazioniAvverse")
public class ReazioneAvversa implements Serializable
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

    public Set<Diagnosi> getDiagnosi() { return EntitiesHelper.unmodifiableListReturn(diagnosi); }

    public Set<Pasto> getPasti() { return EntitiesHelper.unmodifiableListReturn(pasti); }

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
}
