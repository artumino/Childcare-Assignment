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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "bambino")
    private List<RegistroPresenze> presenze = new ArrayList<>();

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER) //Non posso fare confronti se è LAZY :S
    @JoinColumn(name = "Pediatra_FK")
    private Pediatra pediatra;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "TutoriLegali",
            joinColumns = { @JoinColumn(name = "Bambino_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Genitore_FK") }
    )
    private Set<Genitore> genitori = new HashSet<>();

    @ManyToMany(mappedBy = "bambini", fetch = FetchType.EAGER)
    private List<Contatto> contatti = new ArrayList<>();

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

    public void setPediatra(Pediatra pediatra) { this.pediatra = pediatra; }

    public Gruppo getGruppo() { return gruppo; }

    public void setGruppo(Gruppo gruppo) { this.gruppo = gruppo; }

    public void addGenitore(Genitore g) { genitori.add(g); }

    public void removeGenitore(Genitore g) { genitori.remove(g); }

    public List<RegistroPresenze> getPresenze()
    {
        List<RegistroPresenze> ritorno = new ArrayList<>();
        Collections.copy(ritorno, presenze);
        Collections.unmodifiableList(ritorno);
        return ritorno;
    }

    public Set<Genitore> getGenitori()
    {
        Set<Genitore> ritorno = new HashSet<>(genitori);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    public List<Contatto> getContatti()
    {
        List<Contatto> ritorno = new ArrayList<>();
        Collections.copy(ritorno, contatti);
        Collections.unmodifiableList(ritorno);
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
