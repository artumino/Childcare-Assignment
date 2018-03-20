package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Pasti")
public class Pasto implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

    @Column(nullable = false, length = 20)
    private String Nome;

    @Column(length = 50)
    private String Descrizione;

    //endregion

    //region Relazioni

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Fornitore_Pasto",
            joinColumns = { @JoinColumn(name = "Pasto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Fornitore_FK") }
    )
    private List<Fornitore> fornitori;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ReazioneAvversa_Pasto",
            joinColumns = { @JoinColumn(name = "Pasto_FK") },
            inverseJoinColumns = { @JoinColumn(name = "ReazioneAvversa_FK") }
    )
    private List<ReazioneAvversa> reazione;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pasto")
    private List<QuantitaPasto> quantitaPasto;

    //endregion

    //region Metodi

    public Pasto() { }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public List<Fornitore> getFornitori() {
        return fornitori;
    }

    public void setFornitori(List<Fornitore> fornitori) {
        this.fornitori = fornitori;
    }

    public List<ReazioneAvversa> getReazione() {
        return reazione;
    }

    public void setReazione(List<ReazioneAvversa> reazione) {
        this.reazione = reazione;
    }

    public List<QuantitaPasto> getQuantitaPasto() {
        return quantitaPasto;
    }

    public void setQuantitaPasto(List<QuantitaPasto> quantitaPasto) {
        this.quantitaPasto = quantitaPasto;
    }

    //endregion
}
