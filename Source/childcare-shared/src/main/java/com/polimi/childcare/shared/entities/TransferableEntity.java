package com.polimi.childcare.shared.entities;

import java.io.Serializable;
import java.util.List;

public abstract class TransferableEntity implements IIdentificable, Serializable
{

    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     * @param processed Lista usata per la ricorsione, tiene traccia degli oggetti già processati in questa ricorsione
     */
    public abstract void toDTO(List<Object> processed);


    /**
     * Ritorna true se l'istanza di questo oggetto è un DTO
     * @return true se l'oggetto ed i suoi figli sono DTO, false in caso opposto
     */
    public abstract boolean isDTO();

    /**
     * Calcola un hash dell'istanza di una classe per controllare eventuali conflitti
     * @return Codice hash che indica lo stato di consistenza dell'entità (permette una basilare risoluzione di conflitti)
     */
    public abstract int consistecyHashCode();
}
