package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ReazioniAvverse")
public class ReazioneAvversa implements Serializable
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reazioneAvversa")
    private List<Diagnosi> diagnosi;

    @ManyToMany(mappedBy = "reazione")
    private List<Pasto> pasti;

    //endregion

    //region Metodi

    public ReazioneAvversa() { }

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

    public List<Diagnosi> getDiagnosi() {
        return diagnosi;
    }

    public void setDiagnosi(List<Diagnosi> diagnosi) {
        this.diagnosi = diagnosi;
    }

    public List<Pasto> getPasti() {
        return pasti;
    }

    public void setPasti(List<Pasto> pasti) {
        this.pasti = pasti;
    }

    //endregion
}
