package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Diagnosi")
public class Diagnosi extends TransferableEntity implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column()
    private boolean Allergia; //boolean non può essere null

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Persona_FK")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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

    public void unsafeSetID(int ID) { this.ID = ID; }

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
    //In questo caso usiamo anche reazioneAvversa.ID perchè se per caso ho istanze non ancora inserite nel DB di diagnosi diverse, l'equals essendo solo legato all'ID fallirà nel distinguerle
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Diagnosi)) return false;
        Diagnosi diagnosi = (Diagnosi) o;
        return getID() == diagnosi.getID() && Objects.equals(getReazioneAvversa(), diagnosi.getReazioneAvversa());
    }

    //endregion


    //region DTO


    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO(List<Object> processed)
    {
        persona = DTOUtils.objectToDTO(persona, processed);
        reazioneAvversa = DTOUtils.objectToDTO(reazioneAvversa, processed);
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(persona) && DTOUtils.isDTO(reazioneAvversa);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion


    //region Relations Interfaces

    public IManyToOne<Persona, Diagnosi> asDiagnosiPersonaRelation()
    {
        return new IManyToOne<Persona, Diagnosi>() {

            @Override
            public Diagnosi getItem() {
                return Diagnosi.this;
            }

            @Override
            public void setRelation(Persona item) {
                setPersona(item);
            }

            @Override
            public Persona getRelation() {
                return getPersona();
            }

            @Override
            public IOneToMany<Diagnosi, Persona> getInverse(Persona item) {
                return item.asPersonaDiagnosiRelation();
            }
        };
    }

    public IManyToOne<ReazioneAvversa, Diagnosi> asDiagnosiReazioneAvversaRelation()
    {
        return new IManyToOne<ReazioneAvversa, Diagnosi>() {
            @Override
            public Diagnosi getItem() {
                return Diagnosi.this;
            }

            @Override
            public void setRelation(ReazioneAvversa item) {
                setReazioneAvversa(item);
            }

            @Override
            public ReazioneAvversa getRelation() {
                return getReazioneAvversa();
            }

            @Override
            public IOneToMany<Diagnosi, ReazioneAvversa> getInverse(ReazioneAvversa item) {
                return item.asReazioniAvverseDiagnosiRelation();
            }
        };
    }

    //endregion
}
