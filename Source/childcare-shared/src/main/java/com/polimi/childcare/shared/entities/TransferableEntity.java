package com.polimi.childcare.shared.entities;

public abstract class TransferableEntity
{

    /**
     * Utilizzato per create oggetti non dipendenti dalle implementazioni di Hibernate
     * ATTENZIONE: Questo metodo distrugge il REP della classe(che diventa solo una struttura per scambiare dati)
     */
    public abstract void toDTO();


    /**
     * Ritorna true se l'istanza di questo oggetto è un DTO
     */
    public abstract boolean isDTO();
}
