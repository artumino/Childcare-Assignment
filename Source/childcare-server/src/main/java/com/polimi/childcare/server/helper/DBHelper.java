package com.polimi.childcare.server.helper;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.TransferableEntity;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwned;
import com.polimi.childcare.shared.utils.ContainsHelper;
import org.hibernate.Hibernate;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DBHelper
{
    public static <T> T objectInitialize(T object)
    {

        if(!Hibernate.isInitialized(object))
            Hibernate.initialize(object);
        T unproxiedobj = (T)Hibernate.unproxy(object);
        return unproxiedobj;
    }

    public static <T> void recursiveObjectInitialize(T object)
    {
        //Ottengo tutti i possibili field
        List<Field> fieldList = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredFields()));

        Class tmpClass = object.getClass().getSuperclass();
        while (tmpClass != null) {
            fieldList.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }

        //Cerco di inizializzare ogni field
        boolean normallyAccessible = false;
        for(Field field : fieldList)
        {
            try {
                //Addio Private :(
                if(!(normallyAccessible = field.isAccessible()))
                    field.setAccessible(true);

                Object fieldValue = field.get(object);
                field.set(object, objectInitialize(fieldValue));

                //Ben tornato private :)
                if(!normallyAccessible)
                    field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> void recursiveIterableIntitialize(Iterable<T> iterable)
    {
        for (T item : iterable) {
            recursiveObjectInitialize(item);
        }
    }

    public static <T> void filterAdd(Stream<T> query, List<Comparator<T>> param, List<Predicate<T>> filters)
    {
        if(filters != null)
        {
            for (Predicate<T> entry : filters) {
                query = query.filter(entry);
            }
        }

        if(param != null)
        {
            for (Comparator<T> entry : param) {
                query = query.sorted(entry);
            }
        }
    }

    /**
     * Metodo generico che date 2 istanze delle stesso oggetto (una detached ma modificata, una attached che rappresenta quell'oggetto nella
     * sessione attuale sul DB) effettua l'update corretto della relazione specificata
     * @param detachedEntityRelation Relazione tra Oggetto Detached(Modificato) e Contatti (Detached)
     * @param attachedEntity Relazione tra Oggetto Attached (Dati vecchi) e Contatti (Attached)
     * @param ownerClass Classe dell'oggetto della relazione Owner
     * @param session Sessione attuale del DataBase
     * @param <T> Tipo dell'oggetto Owner
     * @param <U> Tipo dell'oggetto Owned
     */
    public static <T extends TransferableEntity, U extends TransferableEntity>
                void updateManyToManyOwned(IManyToManyOwned<U,T> detachedEntityRelation, IManyToManyOwned<U,T> attachedEntity,
                                                            Class<U> ownerClass, DatabaseSession.DatabaseSessionInstance session)
    {
        //Se non sono owner non mi preoccupo tanto dei dati ma delle relazioni. Non avendo potere sulle relazioni devo fare l'update di ogni
        //U nuovo e rimosso (quelli che non sono stati toccati possono rimanere nella lista anche con dati sbgliati dato che non verranno modificati da hibernate)
        Set<U> detachedItems = detachedEntityRelation.getUnmodifiableRelation();
        Set<U> attachedItems = attachedEntity.getUnmodifiableRelation();
        
        for (U unupdated : detachedItems) //Sottraggo gli U che sono dell'istanza sul DB e quella detached (che sono gli U le cui relazioni non sono state modificate)
            if(ContainsHelper.containsHashCode(attachedItems, unupdated)) //Se il U è in entrambe le liste(non ho modificato alcuna relazione, allora lo rimuovo da entrambe
            {
                detachedEntityRelation.unsafeRemoveRelation(unupdated);
                attachedEntity.unsafeRemoveRelation(unupdated);
            }
        //Dopo questa operazione in detachedEntityRelation.getUnmodifiableRelation() ho gli U aggiunti
        //in attachedEntity.getUnmodifiableRelation() ho quelli che ho rimosso dalla relazione
        Set<U> inserted = detachedEntityRelation.getUnmodifiableRelation();
        Set<U> removed = attachedEntity.getUnmodifiableRelation();

        //Per tutti gli U aggiunti, aggiungo la relazione
        for(U unupdated : inserted)
        {
            //Gli U inseriti non li ho ancora presi dal DB quindi è buona norma aspettarsi che potrebbero essere cambiati, quindi oltre ad impostare la relazione
            //sistemo anche l'integrità dei dati
            U updated = session.getByID(ownerClass, unupdated.getID());
            detachedEntityRelation.unsafeRemoveRelation(unupdated);

            //Se non è stato cancellato il U inserito, allora riaggiungo la versione aggiornata e lo aggiorno sul DB con la nuova relazione
            if (updated != null) {
                detachedEntityRelation.getInverse(updated).removeRelation(detachedEntityRelation.getItem());
                session.update(updated);
                detachedEntityRelation.unsafeAddRelation(updated);
            }
        }

        //Per tutti gli U rimossi(che sono presi da attachedEntity, quindi sono già consistenti sul DB mi limito a rimuovere la relazione ed aggiornarli)
        for (U rem : removed)
        {
            attachedEntity.getInverse(rem).removeRelation(attachedEntity.getItem());
            session.update(rem);
        }

        //Giusto per rimanere consistenti riaggiungo la versione aggiornata degli U che non hanno cambiato relazione
        //Prendendoli dunque da attachedItems
        for(U item : attachedItems)
            if(ContainsHelper.containsHashCode(detachedItems, item))
                attachedEntity.unsafeAddRelation(item);
    }
}
