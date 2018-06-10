package com.polimi.childcare.server.database.dao;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.implementations.*;
import com.polimi.childcare.shared.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Singleton per la creazione di Dao data la classe di appartenenza
 */
public class DaoFactory
{
    //region Singleton

    private static DaoFactory instance;
    public static DaoFactory getInstance() {
        return instance != null ? instance : (instance = new DaoFactory());
    }

    //endregion

    private HashMap<Class,Class<? extends ICommonDao>> daoMap;

    public DaoFactory()
    {
        daoMap = new HashMap<>();

        daoMap.put(Addetto.class, AddettoDaoImpl.class);
        daoMap.put(Bambino.class, BambinoDaoImpl.class);
        daoMap.put(Contatto.class, ContattoDaoImpl.class);
        daoMap.put(Diagnosi.class, DiagnosiDaoImpl.class);
        daoMap.put(Fornitore.class, FornitoreDaoImpl.class);
        daoMap.put(Genitore.class, GenitoreDaoImpl.class);
        daoMap.put(Gita.class, GitaDaoImpl.class);
        daoMap.put(Gruppo.class, GruppoDaoImpl.class);
        daoMap.put(Menu.class, MenuDaoImpl.class);
        daoMap.put(MezzoDiTrasporto.class, MezzoDiTrasportoDaoImpl.class);
        daoMap.put(Pasto.class, PastoDaoImpl.class);
        daoMap.put(Pediatra.class, PediatraDaoImpl.class);
        daoMap.put(PianoViaggi.class, PianoViaggiDaoImpl.class);
        daoMap.put(ReazioneAvversa.class, ReazioneAvversaDaoImpl.class);
        daoMap.put(RegistroPresenze.class, RegistroPresenzeDaoImpl.class);

    }

    /**
     * Ottiene un'implementazione opportuna di un Dao per la classe specificata
     * @param tClass Classe dell'entità di cui si vuole ottenere il DAO
     * @param sessionInstance Sessione attuale sul Database
     * @param <T> Tipo dell'entità del Dao
     * @return {@code ICommonDao<T>} con una opportuna implementazione del DAO
     */
    public <T> ICommonDao<T> getDao(Class<T> tClass, DatabaseSession.DatabaseSessionInstance sessionInstance)
    {
        try {
            return daoMap.get(tClass).getConstructor(DatabaseSession.DatabaseSessionInstance.class).newInstance(sessionInstance);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
