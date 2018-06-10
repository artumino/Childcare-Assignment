package com.polimi.childcare.shared.entities;

import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Tappe")
public class Tappa extends TransferableEntity
{
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false, length = 100)
    private String Luogo;

    //endregion

    //region Relazioni

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Gita_FK")
    private Gita gita;

    //endregion


    //region Metodi

    public Tappa()
    {
    }

    public Tappa(String luogo, Gita gita) {
        Luogo = luogo;
        this.gita = gita;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
    }

    public String getLuogo() {
        return Luogo;
    }

    public void setLuogo(String luogo) {
        Luogo = luogo;
    }

    public Gita getGita() {
        return gita;
    }

    public void setGita(Gita gita) {
        this.gita = gita;
    }


    @Override
    public int hashCode() { return Objects.hash(ID, Tappa.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tappa)) return false;
        Tappa tappa = (Tappa) o;
        return getID() == tappa.getID() &&
                getLuogo().equals(tappa.getLuogo());
    }

    //endregion


    //region DTO
    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    @Override
    public void toDTO(List<Object> processed) {
        gita = DTOUtils.objectToDTO(gita, processed);
    }

    @Override
    public boolean isDTO() {
        return DTOUtils.isDTO(gita);
    }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    public IManyToOne<Gita, Tappa> asTappaGitaRelation()
    {
        return new IManyToOne<Gita, Tappa>() {
            @Override
            public Tappa getItem() {
                return Tappa.this;
            }

            @Override
            public void setRelation(Gita item) {
                setGita(item);
            }

            @Override
            public Gita getRelation() {
                return getGita();
            }

            @Override
            public IOneToMany<Tappa, Gita> getInverse(Gita item) {
                return item.asGitaTappeRelation();
            }

        };
    }
}
