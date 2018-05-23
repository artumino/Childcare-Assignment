package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Gruppi")
public class Gruppo extends TransferableEntity implements Serializable
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gruppo")
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

    public void unsafeAddBambino(Bambino b) { bambini.add(b); }

    public void unsafeRemoveBambino(Bambino b) { bambini.remove(b); }

    public void unsafeAddPianoViaggi(PianoViaggi p) { pianoviaggi.add(p); }

    public void unsafeRemovePianoViaggi(PianoViaggi p) { pianoviaggi.remove(p); }

    public Set<Bambino> getBambini() { return EntitiesHelper.unmodifiableListReturn(bambini); }

    public Set<PianoViaggi> getPianoviaggi() { return EntitiesHelper.unmodifiableListReturn(pianoviaggi); }

    @Override
    public int hashCode() { return Objects.hash(ID, Gruppo.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gruppo)) return false;
        Gruppo gruppo = (Gruppo) o;
        return getID() == gruppo.getID();
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
        //Aggiorna figli
        sorvergliante = DTOUtils.objectToDTO(sorvergliante);
        bambini = DTOUtils.iterableToDTO(bambini);
        pianoviaggi = DTOUtils.iterableToDTO(pianoviaggi);

        bambini = this.getBambini();
        pianoviaggi = this.getPianoviaggi();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(pianoviaggi) && DTOUtils.isDTO(bambini) && DTOUtils.isDTO(sorvergliante);
    }

    //endregion
}
