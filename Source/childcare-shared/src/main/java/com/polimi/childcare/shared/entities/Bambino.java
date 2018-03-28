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

    public void addGenitore(Genitore g){ genitori.add(g); }//Poi va fatto update del Database

    public void removeGenitore(Genitore g){ genitori.remove(g); }//Poi va fatto update del Database

    public void addContatto(Contatto c){ contatti.add(c); }//Poi va fatto update del Database

    public void removeContatto(Contatto c){ contatti.remove(c); }//Poi va fatto update del Database

    public void addGruppo(Gruppo g){ gruppi.add(g); }//Poi va fatto update del Database

    public void removeGruppo(Gruppo g){ gruppi.remove(g); }//Poi va fatto update del Database

    public List<RegistroPresenze> getPresenze() { return presenze; }

    public List<Genitore> getGenitori() { return genitori; }

    public List<Contatto> getContatti() { return contatti; }

    public List<Gruppo> getGruppi() { return gruppi;
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
