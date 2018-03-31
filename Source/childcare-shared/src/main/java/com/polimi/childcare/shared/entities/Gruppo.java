package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Gruppi")
public class Gruppo implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;
    //Qua completeremo con ID Tutore e ID Bambino esterni

    //endregion

    //region Relazioni
    @OneToOne(optional = false, cascade = CascadeType.REFRESH)
    private Addetto sorvergliante;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gruppo")
    private List<Bambino> bambini;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gruppo")
    private List<PianoViaggi> pianoviaggi;

    //endregion

    //region Metodi

    public Gruppo() { }

    public Gruppo(Addetto sorvergliante) {
        this.sorvergliante = sorvergliante;
    }

    public int getID() {
        return ID;
    }

    public Addetto getSorvergliante() {
        return sorvergliante;
    }

    public void setSorvergliante(Addetto sorvergliante) {
        this.sorvergliante = sorvergliante;
    }

    public void addBambino(Bambino b)
    {
        if(bambini == null)
            bambini = new ArrayList<>();
        bambini.add(b);
    }   //Poi va fatto update del Database

    public void removeBambino(Bambino b)
    {
        if(bambini != null)
            bambini.remove(b);
    }   //Poi va fatto update del Database

    public void addViaggio(PianoViaggi p)
    {
        if(pianoviaggi == null)
            pianoviaggi = new ArrayList<>();
        pianoviaggi.add(p);
    }

    public void removeViaggio(PianoViaggi p)
    {
        if(pianoviaggi != null)
            pianoviaggi.remove(p);
    }

    public List<Bambino> getBambini() {
        return bambini;
    }

    public List<PianoViaggi> getPianoviaggi() {
        return pianoviaggi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gruppo)) return false;
        Gruppo gruppo = (Gruppo) o;
        return getID() == gruppo.getID(); //&&
                //getSorvergliante().equals(gruppo.getSorvergliante());
    }

    //endregion
}
