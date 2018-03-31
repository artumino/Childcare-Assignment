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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bambino")
    private List<RegistroPresenze> presenze;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER) //Non posso fare confronti se è LAZY :S
    @JoinColumn(name = "Pediatra_FK")
    private Pediatra pediatra;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "TutoriLegali",
            joinColumns = { @JoinColumn(name = "Bambino_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Genitore_FK") }
    )
    private Set<Genitore> genitori;

    @ManyToMany(mappedBy = "bambini")
    private List<Contatto> contatti;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public void setPediatra(Pediatra pediatra) {
        this.pediatra = pediatra;
    }

    public void addGenitore(Genitore g)
    {
        if(genitori == null)
            genitori = new HashSet<>();
        genitori.add(g);
    }//Poi va fatto update del Database

    public void removeGenitore(Genitore g)
    {
        if(genitori != null)
            genitori = new HashSet<>();
        genitori.remove(g);
    }//Poi va fatto update del Database

    public void addContatto(Contatto c)
    {
        if(contatti == null)
            contatti = new ArrayList<>();
        contatti.add(c);
    }//Poi va fatto update del Database

    public void removeContatto(Contatto c)
    {
        if(contatti != null)
            contatti.remove(c);
    }//Poi va fatto update del Database

    public void addPresenza(RegistroPresenze r)
    {
        if(presenze == null)
            presenze = new ArrayList<>();
        presenze.add(r);
    }//Poi va fatto update del Database

    public void removePresenza(RegistroPresenze r)
    {
        if(presenze != null)
            presenze.remove(r);
    }//Poi va fatto update del Database

    public List<RegistroPresenze> getPresenze() { return presenze; }

    public Set<Genitore> getGenitori() { return genitori; }

    public List<Contatto> getContatti() { return contatti; }

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
