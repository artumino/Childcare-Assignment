package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gita")
    private List<PianoViaggi> pianiViaggi;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gita")
    private List<RegistroPresenze> registriPresenze;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gita)) return false;
        Gita gita = (Gita) o;
        return getID() == gita.getID() &&
                getCosto() == gita.getCosto() &&
                Objects.equals(getDataInizio(), gita.getDataInizio()) &&
                Objects.equals(getDataFine(), gita.getDataFine()) &&
                Objects.equals(getLuogo(), gita.getLuogo());
    }

    //endregion
}
