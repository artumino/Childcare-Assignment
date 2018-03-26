package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
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

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Gruppo_Bambino",
            joinColumns = { @JoinColumn(name = "Gruppo_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Bambino_FK") }
    )
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

    public List<Bambino> getBambini() {
        return bambini;
    }

    public void setBambini(List<Bambino> bambini) {
        this.bambini = bambini;
    }

    public List<PianoViaggi> getPianoviaggi() {
        return pianoviaggi;
    }

    public void setPianoviaggi(List<PianoViaggi> pianoviaggi) {
        this.pianoviaggi = pianoviaggi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gruppo)) return false;
        Gruppo gruppo = (Gruppo) o;
        return getID() == gruppo.getID() &&
                Objects.equals(getSorvergliante(), gruppo.getSorvergliante());
    }

    //endregion
}
