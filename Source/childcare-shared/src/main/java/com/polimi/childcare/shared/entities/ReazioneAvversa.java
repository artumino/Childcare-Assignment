package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    private List<Diagnosi> diagnosi = new ArrayList<>();

    @ManyToMany(mappedBy = "reazione", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Pasto> pasti = new ArrayList<>();

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

    public List<Diagnosi> getDiagnosi()
    {
        List<Diagnosi> ritorno = new ArrayList<>();
        Collections.copy(ritorno, diagnosi);
        Collections.unmodifiableList(ritorno);
        return ritorno;
    }

    public List<Pasto> getPasti()
    {
        List<Pasto> ritorno = new ArrayList<>();
        Collections.copy(ritorno, pasti);
        Collections.unmodifiableList(ritorno);
        return ritorno;
    }

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
