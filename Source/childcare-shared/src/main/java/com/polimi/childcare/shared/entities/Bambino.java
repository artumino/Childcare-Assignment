package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Bambini")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Bambino extends Persona
{
    //region Relazioni

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bambino")
    private List<RegistroPresenze> presenze;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "Pediatra_FK")
    private Pediatra pediatra;

    @ManyToMany(mappedBy = "bambini")
    private List<Genitore> genitori;

    @ManyToMany(mappedBy = "bambini")
    private List<Contatto> contatti;

    @ManyToMany(mappedBy = "bambini")
    private List<Gruppo> gruppi;

    //endregion

    //region Metodi


    public Bambino() { }

    public Bambino(String nome, String cognome, String codiceFiscale, Date dataNascita, String stato, String comune, String provincia, String cittadinanza, String residenza, byte sesso)
    {
        super(nome, cognome, codiceFiscale, dataNascita, stato, comune, provincia, cittadinanza, residenza, sesso);
    }

    public Pediatra getPediatra() {
        return pediatra;
    }

    public void setPediatra(Pediatra pediatra) {
        this.pediatra = pediatra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bambino)) return false;
        if (!super.equals(o)) return false;
        Bambino bambino = (Bambino) o;
        return Objects.equals(getPediatra(), bambino.getPediatra());
    }

    //endregion
}
