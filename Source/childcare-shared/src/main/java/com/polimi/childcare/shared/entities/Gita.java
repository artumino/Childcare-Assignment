package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Gite")
public class Gita extends TransferableEntity implements Serializable
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    private LocalDate DataInizio;

    @Column(nullable = false)
    private LocalDate DataFine;

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

    public Gita(LocalDate dataInizio, LocalDate dataFine, String luogo, int costo) {
        DataInizio = dataInizio;
        DataFine = dataFine;
        Luogo = luogo;
        Costo = costo;
    }

    public int getID() {
        return ID;
    }

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
    }

    public LocalDate getDataInizio() {
        return DataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        DataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return DataFine;
    }

    public void setDataFine(LocalDate dataFine) {
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

    public void unsafeAddPianoViaggi(PianoViaggi p) { pianiViaggi.add(p); }

    public void unsafeRemovePianoViaggi(PianoViaggi p) { pianiViaggi.remove(p); }

    public void unsafeAddRegistroPresenza(RegistroPresenze rp) { registriPresenze.add(rp); }

    public void unsafeRemoveRegistroPresenza(RegistroPresenze rp) { registriPresenze.remove(rp); }

    public Set<PianoViaggi> getPianiViaggi() { return EntitiesHelper.unmodifiableListReturn(pianiViaggi); }

    public Set<RegistroPresenze> getRegistriPresenze() { return EntitiesHelper.unmodifiableListReturn(registriPresenze); }

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

    //region DTO


    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO(List<Object> processed)
    {
        pianiViaggi = DTOUtils.iterableToDTO(pianiViaggi, processed);
        registriPresenze = DTOUtils.iterableToDTO(registriPresenze, processed);

        pianiViaggi = getPianiViaggi();
        registriPresenze = getRegistriPresenze();
    }

    @Override
    public boolean isDTO()
    {
        return DTOUtils.isDTO(pianiViaggi) && DTOUtils.isDTO(registriPresenze);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    public IOneToMany<PianoViaggi, Gita> asGitaPianiViaggioRelation()
    {
        return new IOneToMany<PianoViaggi, Gita>() {
            @Override
            public Gita getItem() {
                return Gita.this;
            }

            @Override
            public void unsafeAddRelation(PianoViaggi item) {
                unsafeAddPianoViaggi(item);
            }

            @Override
            public void unsafeRemoveRelation(PianoViaggi item) {
                unsafeRemovePianoViaggi(item);
            }

            @Override
            public Set<PianoViaggi> getUnmodifiableRelation() {
                return getPianiViaggi();
            }

            @Override
            public IManyToOne<Gita, PianoViaggi> getInverse(PianoViaggi item) {
                return item.asPianiViaggioGitaRelation();
            }
        };
    }

    public IOneToMany<RegistroPresenze, Gita> asGitaRegistroPresenzeRelation()
    {
        return new IOneToMany<RegistroPresenze, Gita>() {
            @Override
            public Gita getItem() {
                return Gita.this;
            }

            @Override
            public void unsafeAddRelation(RegistroPresenze item) {
                unsafeAddRegistroPresenza(item);
            }

            @Override
            public void unsafeRemoveRelation(RegistroPresenze item) {
                unsafeRemoveRegistroPresenza(item);
            }

            @Override
            public Set<RegistroPresenze> getUnmodifiableRelation() {
                return getRegistriPresenze();
            }

            @Override
            public IManyToOne<Gita, RegistroPresenze> getInverse(RegistroPresenze item) {
                return item.asRegistroPresenzeGitaRelation();
            }
        };
    }
}
