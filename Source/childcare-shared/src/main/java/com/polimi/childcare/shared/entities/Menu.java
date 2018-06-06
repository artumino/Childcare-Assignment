package com.polimi.childcare.shared.entities;
import com.polimi.childcare.shared.dto.DTOUtils;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwned;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwner;
import com.polimi.childcare.shared.utils.EntitiesHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.*;

@Entity
@Table(name = "Menu")
public class Menu extends TransferableEntity implements Serializable
{
    /**
     * Enum comodo per impostare la ricorrenza del menu
     */
    public enum DayOfWeekFlag
    {
        Lun(1, DayOfWeek.MONDAY),
        Mar(2, DayOfWeek.TUESDAY),
        Mer(4, DayOfWeek.WEDNESDAY),
        Gio(8, DayOfWeek.THURSDAY),
        Ven(16, DayOfWeek.FRIDAY),
        Sab(32, DayOfWeek.SATURDAY),
        Dom(64, DayOfWeek.SUNDAY);

        private int flag;
        private DayOfWeek dayOfWeek;

        DayOfWeekFlag(int flag, java.time.DayOfWeek dayOfWeek)
        {
            this.flag = flag;
            this.dayOfWeek = dayOfWeek;
        }

        public int getFlag() {
            return flag;
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }
    }
    //region Attributi

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int ID;

    @Column(nullable = false)
    private String Nome;

    @Column(nullable = false)
    private boolean Attivo;

    @Column(nullable = false)
    private int Ricorrenza;

    //endregion

    //region Relazioni

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "MenuPasti",
            joinColumns = { @JoinColumn(name = "Menu_FK") },
            inverseJoinColumns = { @JoinColumn(name = "Pasto_FK") }
    )
    private Set<Pasto> pasti = new HashSet<>();

    //endregion

    //region Metodi

    public Menu() { }

    public Menu(String nome, boolean attivo, int ricorrenza)
    {
        Nome = nome;
        this.Attivo = attivo;
        Ricorrenza = ricorrenza;
    }

    public int getID() {
        return ID;
    }

    @Override
    public void unsafeSetID(int ID) {
        this.ID = ID;
    }

    public boolean isAttivo() { return Attivo; }

    public void setAttivo(boolean attivo) { this.Attivo = attivo; }

    public int getRicorrenza() { return Ricorrenza; }

    public void setRicorrenza(int ricorrenza) { Ricorrenza = ricorrenza; }

    public String getNome() { return Nome; }

    public void setNome(String nome) { Nome = nome; }

    public boolean isRecurringDuringDayOfWeek(DayOfWeekFlag dayOfWeekFlag) { return (this.getRicorrenza() & dayOfWeekFlag.getFlag()) != 0; }

    public void addRicorrenza(DayOfWeekFlag dayOfWeekFlag) { this.setRicorrenza(getRicorrenza() | dayOfWeekFlag.getFlag());}

    public void removeRicorrenza(DayOfWeekFlag dayOfWeekFlag) { if(isRecurringDuringDayOfWeek(dayOfWeekFlag)) this.setRicorrenza(getRicorrenza() - dayOfWeekFlag.getFlag());}

    public void addPasto(Pasto p) { pasti.add(p); }

    public void removePasto(Pasto p) { pasti.remove(p); }

    public Set<Pasto> getPasti() { return EntitiesHelper.unmodifiableListReturn(pasti); }

    @Override
    public int hashCode() { return Objects.hash(ID, Menu.class); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return getID() == menu.getID() &&
                getRicorrenza() == menu.getRicorrenza() &&
                isAttivo() == menu.isAttivo();
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
        pasti = DTOUtils.iterableToDTO(pasti, processed);

        pasti = this.getPasti();
    }

    @Override
    public boolean isDTO() { return DTOUtils.isDTO(pasti); }

    @Override
    public int consistecyHashCode() {
        return 0;
    }

    //endregion

    //region Relations Interfaces

    public IManyToManyOwner<Pasto, Menu> asMenuPastoRelation()
    {
        return new IManyToManyOwner<Pasto, Menu>() {
            @Override
            public Menu getItem() {
                return Menu.this;
            }

            @Override
            public void addRelation(Pasto item) {
                addPasto(item);
            }

            @Override
            public void removeRelation(Pasto item) {
                removePasto(item);
            }

            @Override
            public Set<Pasto> getUnmodifiableRelation() {
                return getPasti();
            }

            @Override
            public IManyToManyOwned<Menu, Pasto> getInverse(Pasto item) {
                return item.asPastoMenuRelation();
            }
        };
    }

    //endregion
}
