package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Gruppi")
public class Gruppo implements Serializable, ITransferable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;
    //Qua completeremo con ID Tutore e ID Bambino esterni

    //endregion

    //region Relazioni
    @OneToOne(optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Addetto sorvergliante;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "gruppo")
    private Set<Bambino> bambini  = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "gruppo")
    private Set<PianoViaggi> pianoviaggi = new HashSet<>();

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

    public Set<Bambino> getBambini()
    {
        Set<Bambino> ritorno = new HashSet<>(bambini);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    public Set<PianoViaggi> getPianoviaggi()
    {
        Set<PianoViaggi> ritorno = new HashSet<>(pianoviaggi);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, Gruppo.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gruppo)) return false;
        Gruppo gruppo = (Gruppo) o;
        return getID() == gruppo.getID(); //&&
                //getSorvergliante().equals(gruppo.getSorvergliante());
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
        DTOUtils.objectToDTO(sorvergliante);

        bambini = this.getBambini();
        pianoviaggi = this.getPianoviaggi();

        //Aggiorna figli
        DTOUtils.iterableToDTO(bambini);
        DTOUtils.iterableToDTO(pianoviaggi);
    }

    @Override
    public boolean isDTO()
    {
        return (pianoviaggi instanceof HashSet) && (bambini instanceof HashSet) && DTOUtils.isDTO(sorvergliante);
    }

    //endregion
}
