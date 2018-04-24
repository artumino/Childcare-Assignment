package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Gite")
public class Gita implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date DataInizio;

    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date DataFine;

    @Column(nullable = false, length = 50)
    private String Luogo;

    @Column(nullable = false)
    private int Costo;

    //endregion

    //region Relazioni

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "gita")
    private Set<PianoViaggi> pianiViaggi = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "gita")
    private Set<RegistroPresenze> registriPresenze = new HashSet<>();

    //endregion

    //region Metodi

    public Gita() { }

    public Gita(Date dataInizio, Date dataFine, String luogo, int costo) {
        DataInizio = dataInizio;
        DataFine = dataFine;
        Luogo = luogo;
        Costo = costo;
    }

    public int getID() {
        return ID;
    }

    public Date getDataInizio() {
        return DataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        DataInizio = dataInizio;
    }

    public Date getDataFine() {
        return DataFine;
    }

    public void setDataFine(Date dataFine) {
        DataFine = dataFine;
    }

    public String getLuogo() {
        return Luogo;
    }

    public void setLuogo(String luogo) {
        Luogo = luogo;
    }

    public int getCosto() {
        return Costo;
    }

    public Set<PianoViaggi> getPianiViaggi()
    {
        Set<PianoViaggi> ritorno = new HashSet<>(pianiViaggi);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    public Set<RegistroPresenze> getRegistriPresenze()
    {
        Set<RegistroPresenze> ritorno = new HashSet<>(registriPresenze);
        Collections.unmodifiableSet(ritorno);
        return ritorno;
    }

    @Override
    public int hashCode() { return Objects.hash(ID, Gita.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gita)) return false;
        Gita gita = (Gita) o;
        return getID() == gita.getID() &&
                getCosto() == gita.getCosto() &&
                getDataInizio().compareTo(gita.getDataInizio()) == 0 &&
                getDataFine().compareTo(gita.getDataFine()) == 0 &&
                getLuogo().equals(gita.getLuogo());
    }

    //endregion
}
