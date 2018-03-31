package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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

    public void addDiagnosi(Diagnosi d)
    {
        if(diagnosi == null)
            diagnosi = new ArrayList<>();
        diagnosi.add(d);
    }   //Poi va fatto update del Database

    public void removeDiagnosi(Diagnosi d)
    {
        if(diagnosi != null)
            diagnosi.remove(d);
    }   //Poi va fatto update del Database

    public void addPasto(Pasto p)
    {
        if(pasti == null)
            pasti = new ArrayList<>();
        pasti.add(p);
    }  //Poi va fatto update del Database

    public void removePasto(Pasto p)
    {
        if(pasti != null)
            pasti.remove(p);
    }  //Poi va fatto update del Database

    public List<Diagnosi> getDiagnosi() { return diagnosi; }

    public List<Pasto> getPasti() { return pasti; }

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
