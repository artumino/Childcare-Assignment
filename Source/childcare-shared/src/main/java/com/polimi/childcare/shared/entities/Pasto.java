package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Pasti")
public class Pasto implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

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

    public Pasto(String nome, String descrizione)
    {
        Nome = nome;
        Descrizione = descrizione;
    }

    public int getID() {
        return ID;
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

    public void addQuantitaPasto(QuantitaPasto q)
    {
        if(quantitaPasto == null)
            quantitaPasto = new ArrayList<>();
        quantitaPasto.add(q);
    }

    public void removeQuantitaPasto(QuantitaPasto q)
    {
        if(quantitaPasto != null)
            quantitaPasto.remove(q);
    }

    public void addReazione(ReazioneAvversa r)
    {
        if(reazione == null)
            reazione = new ArrayList<>();
        reazione.add(r);
    }

    public void removeReazione(ReazioneAvversa r)
    {
        if(reazione != null)
            reazione.remove(r);
    }

    public void addFornitori(Fornitore f)
    {
        if(fornitori == null)
            fornitori = new ArrayList<>();
        fornitori.add(f);
    }

    public void removeFornitori(Fornitore f)
    {
        if(fornitori != null)
            fornitori.remove(f);
    }

    public List<Fornitore> getFornitori() { return fornitori; }

    public List<ReazioneAvversa> getReazione() { return reazione; }

    public List<QuantitaPasto> getQuantitaPasto() { return quantitaPasto; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pasto)) return false;
        Pasto pasto = (Pasto) o;
        return getID() == pasto.getID() &&
                getNome().equals(pasto.getNome()) &&
                getDescrizione().equals(pasto.getDescrizione());
    }

    //endregion
}
