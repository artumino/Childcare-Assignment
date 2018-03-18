package com.polimi.childcare.shared.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Bambini")
@PrimaryKeyJoinColumn(name="Persona_ID")
public class Bambino extends Persona
{
    //region Relazioni

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bambino")
    private List<RegistroPresenze> presenze;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "Pediatra_FK")
    private Pediatra pediatra;

    @ManyToMany(mappedBy = "bambini")
    private List<Genitore> genitori;

    @ManyToMany(mappedBy = "bambini")
    private List<Contatto> contatti;

    @ManyToMany(mappedBy = "bambini")
    private List<Gruppo> gruppi;

    //endregion
}
