package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "MezziDiTrasporto")
public class MezzoDiTrasporto implements Serializable
{
    //region Attributi

    @Id
    private Long ID;

    @Column(nullable = false, length = 7)   //Targa Italiana
    private String Targa;

    @Column(nullable = false)
    private int Capienza;

    @Column(nullable = false)
    private int NumeroIdentificativo;

    @Column(nullable = false)
    private int CostoOrario;

    //endregion Attributi

    //region Relazioni

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Fornitore_FK")
    private Fornitore fornitore;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mezzo")
    private List<PianoViaggi> pianoViaggi;

    //endregion

    //region Metodi

    public MezzoDiTrasporto() { }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTarga() {
        return Targa;
    }

    public void setTarga(String targa) {
        Targa = targa;
    }

    public int getCapienza() {
        return Capienza;
    }

    public void setCapienza(int capienza) {
        Capienza = capienza;
    }

    public int getNumeroIdentificativo() {
        return NumeroIdentificativo;
    }

    public void setNumeroIdentificativo(int numeroIdentificativo) {
        NumeroIdentificativo = numeroIdentificativo;
    }

    public int getCostoOrario() {
        return CostoOrario;
    }

    public void setCostoOrario(int costoOrario) {
        CostoOrario = costoOrario;
    }

    public Fornitore getFornitore() {
        return fornitore;
    }

    public void setFornitore(Fornitore fornitore) {
        this.fornitore = fornitore;
    }

    //endregion
}
