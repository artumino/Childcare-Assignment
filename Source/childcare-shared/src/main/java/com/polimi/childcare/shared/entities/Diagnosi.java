package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "Diagnosi")
public class Diagnosi implements Serializable, ITransferable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    private boolean Allergia;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Persona_FK")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "ReazioneAvversa_FK")
    private ReazioneAvversa reazioneAvversa;

    //endregion

    //region Metodi

    public Diagnosi() { }

    public Diagnosi(boolean allergia, Persona persona, ReazioneAvversa reazioneAvversa) {
        Allergia = allergia;
        this.persona = persona;
        this.reazioneAvversa = reazioneAvversa;
    }

    public int getID() {
        return ID;
    }

    public boolean isAllergia() {
        return Allergia;
    }

    public void setAllergia(boolean allergia) {
        Allergia = allergia;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public ReazioneAvversa getReazioneAvversa() {
        return reazioneAvversa;
    }

    public void setReazioneAvversa(ReazioneAvversa reazioneAvversa) {
        this.reazioneAvversa = reazioneAvversa;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, Diagnosi.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Diagnosi)) return false;
        Diagnosi diagnosi = (Diagnosi) o;
        return getID() == diagnosi.getID(); //&&
                //isAllergia() == diagnosi.isAllergia() &&
                //getPersona().equals(diagnosi.getPersona()) &&
                //getReazioneAvversa().equals(diagnosi.getReazioneAvversa());
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
        DTOUtils.objectToDTO(persona);
        DTOUtils.objectToDTO(reazioneAvversa);
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(persona) && DTOUtils.isDTO(reazioneAvversa);
    }

    //endregion
}
