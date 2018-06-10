package com.polimi.childcare.server.database.dao;

/**
 * Interfaccia che permette le operazioni basilari di persistenza dei dati su una entità
 * @param <T> Tipo dell'entità a cui questo Dao fa riferimento
 */
public interface ICommonDao<T>
{
    /**
     * Cancella l'elemento rispettando i constraint e facendo gli opportuni cascade
     * @param item Elemento da eliminare
     */
    void delete(T item);


    /**
     * Inserisce l'elemento rispettando i constraint e inserendo gli opportuni figli
     * @param item Elemento da inserire (con eventuali relazioni)
     * @return ID dell'elemento eventualmente inserito
     */
    int insert(T item);

    /**
     * Aggiorna l'elemento rispettando i constraint e aggiornando eventuali relazioni con i figli (non modifica i dati dei figli)
     * @param item Elemento da modificare (con eventuali relazioni)
     */
    void update(T item);
}
