package com.polimi.childcare.shared.entities;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Fornitori")
public class Fornitore implements Serializable
{
    //region Attributi
    @DatabaseField(generatedId = true)
    @Id
    private Long ID;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 50)
    private String RagioneSociale;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 11)  //In Italia 11 cifre, problemi sono con Olanda e Svezia che sono a 12, ma abbiamo detto che deve essere Italiana e iscritta al Registro Imprese
    private String PartitaIVA;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 50)
    private String SedeLegale;

    @DatabaseField(dataType = DataType.STRING, canBeNull = false)
    @Column(nullable = false, length = 15)  //Lascio a 15, non ho trovato un documento ufficiale che ne attesti la lunghezza
    private String NumeroRegistroImprese;

    @DatabaseField(dataType = DataType.STRING)
    @Column(length = 30)
    private String Email;

    @DatabaseField(dataType = DataType.STRING)
    @Column(length = 15)   //10 numeri + 3 di prefisso (oppure 4 se conti due 0 come il +), lascio a 15 per sicurezza
    private String FAX;

    @DatabaseField(dataType = DataType.STRING)
    @Column(length = 27)
    private String IBAN;    //Ancora caso Italiano 27 caratteri

    //Mancano Numeri di Telefono e Prodotti (campi multipli)
    //endregion

    //region Relazioni

    @ManyToMany(mappedBy = "fornitori")
    private List<Pasto> pasti;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fornituramezzi")
    private List<Fornitore> fornitorimezzi;

    //endregion
}
