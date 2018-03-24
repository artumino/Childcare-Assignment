package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ReazioniAvverse")
public class ReazioneAvversa implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int ID;

    @Column(nullable = false, length = 20)
    private String Nome;

    @Column(length = 50)
    private String Descrizione;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reazioneAvversa")
    private List<Diagnosi> diagnosi;

    @ManyToMany(mappedBy = "reazione")
    private List<Pasto> pasti;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReazioneAvversa)) return false;
        ReazioneAvversa that = (ReazioneAvversa) o;
        return getID() == that.getID() &&
                Objects.equals(getNome(), that.getNome()) &&
                Objects.equals(getDescrizione(), that.getDescrizione());
    }

    //endregion
}
