package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Bambini")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Bambino extends Persona
{
    //region Relazioni

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY) //Non posso fare confronti se è LAZY :S
    @JoinColumn(name = "Pediatra_FK")
    private Pediatra pediatra;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "TutoriLegali",
            joinColumns = { @JoinColumn(name = "Bambino_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Genitore_FK") }
    )
    private Set<Genitore> genitori = new HashSet<>();

    @ManyToMany(mappedBy = "bambini", fetch = FetchType.LAZY)
    private Set<Contatto> contatti = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Gruppo_FK")
    private Gruppo gruppo;

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

    public void setPediatra(Pediatra pediatra) { this.pediatra = pediatra; }

    public Gruppo getGruppo() { return gruppo; }

    public void setGruppo(Gruppo gruppo) { this.gruppo = gruppo; }

    public void addGenitore(Genitore g) { genitori.add(g); }

    public void removeGenitore(Genitore g) { genitori.remove(g); }

    public Set<Genitore> getGenitori()
    {
        Set<Genitore> ritorno = new HashSet<>(genitori);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    public Set<Contatto> getContatti()
    {
        Set<Contatto> ritorno = new HashSet<>(contatti);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    @Override
    public boolean equals(Object o) {           //Ora funzionante con Pediatra EAGER
        if (this == o) return true;
        if (!(o instanceof Bambino)) return false;
        if (!super.equals(o)) return false;
        //Bambino bambino = (Bambino) o;

        //E' concettualmente errato controllare anche il pediatra, se io voglio fare Bambino = Bambino mi aspetto solo
        //che abbiano gli attributi uguali ma non che siano anche consistenti sul DB (quello è un controllo da fare altrove)
        return true;//getPediatra().equals(bambino.getPediatra()); //LAZY Error
    }

    //endregion
}
