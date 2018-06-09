package com.polimi.childcare.server.helper;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.TransferableEntity;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwned;
import com.polimi.childcare.shared.entities.relations.IManyToManyOwner;
import com.polimi.childcare.shared.entities.relations.IManyToOne;
import com.polimi.childcare.shared.entities.relations.IOneToMany;
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
        if(object == null)
            return;

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

    public static <T extends TransferableEntity, U extends TransferableEntity>
    void deletedManyToManyOwned(IManyToManyOwned<U,T> detachedDeletedEntityRelation, IManyToManyOwned<U,T> attachedDeletedEntityRelation, Class<U> ownerClass, DatabaseSession.DatabaseSessionInstance session)
    {
        //Rimuove le relazioni di cui non è owner
        for (U updated : attachedDeletedEntityRelation.getUnmodifiableRelation())
        {
            detachedDeletedEntityRelation.unsafeRemoveRelation(updated);
            if (updated != null)
            {
                attachedDeletedEntityRelation.getInverse(updated).removeRelation(attachedDeletedEntityRelation.getItem());
                session.update(updated);
            }
        }
    }

    public static <T extends TransferableEntity, U extends TransferableEntity>
    void updateManyToOne(IManyToOne<U,T> detachedEntityRelation,  Class<U> ownedClass, DatabaseSession.DatabaseSessionInstance session)
    {
        //Qualunque sia U, se non è null prendo l'ID del nuovo U e ne aggiorno i dati con quello sul DB
        if(detachedEntityRelation.getItem() != null)
        {
            //Questo check risolve il caso in cui:
            /*
                Utente A: aggiorna in locale T senza modificare il U (ID=1)
                Utente B: manda al DB una modifica per U con ID = 1 in cui cambia qualche parametro
                Utente A: conclude le modifiche a T e manda la richiesta di set
                   Se non facessimo questo check, relation.getItem() ritornerebbe l'ID di U giusto ma con le vecchie modifiche
                   e quindi l'update andrebbe a modificarne la tabella
                Facendo il controllo, imposto U al valore che ho nel DB quindi ha ID giusto e dati giusti, così facendo l'update non sovrascrive nulla
             */
            U updated = detachedEntityRelation.getRelation() != null ? session.getByID(ownedClass, detachedEntityRelation.getRelation().getID()) : null;
            detachedEntityRelation.setRelation(updated);
        }
    }

    public static <T extends TransferableEntity, U extends TransferableEntity>
    void updateOneToMany(IOneToMany<U,T> detachedEntityRelation, IOneToMany<U,T> attachedEntity,
                               Class<U> ownerClass, DatabaseSession.DatabaseSessionInstance session)
    {
        //Se non sono owner non mi preoccupo tanto dei dati ma delle relazioni. Non avendo potere sulle relazioni devo fare l'update di ogni
        //U nuovo e rimosso (quelli che non sono stati toccati possono rimanere nella lista anche con dati sbgliati dato che non verranno modificati da hibernate)
        Set<U> toUpdate = detachedEntityRelation.getUnmodifiableRelation();
        Set<U> attachedItems = attachedEntity.getUnmodifiableRelation();

        for (U unupdated : toUpdate) //Sottraggo gli U che sono dell'istanza sul DB e quella detached (che sono gli U le cui relazioni non sono state modificate)
            if(ContainsHelper.containsHashCode(attachedItems, unupdated)) //Se il U è in toUpdate allora non è stato rimosso e devo solo aggiornarlo
                attachedEntity.unsafeRemoveRelation(unupdated);

        //Dopo questa operazione in attachedEntity.getUnmodifiableRelation() ho quelli che ho rimosso dalla relazione
        Set<U> removed = attachedEntity.getUnmodifiableRelation();

        //Per tutti gli U aggiunti, aggiorno la relazione
        for(U unupdated : toUpdate)
        {
            //Gli U inseriti non li ho ancora presi dal DB quindi è buona norma aspettarsi che potrebbero essere cambiati, quindi oltre ad impostare la relazione
            //sistemo anche l'integrità dei dati
            U updated = session.getByID(ownerClass, unupdated.getID());
            detachedEntityRelation.unsafeRemoveRelation(unupdated);

            //Se non è stato cancellato il U inserito, allora riaggiungo la versione aggiornata e lo aggiorno sul DB con la nuova relazione
            if (updated != null) {
                detachedEntityRelation.getInverse(updated).setRelation(detachedEntityRelation.getItem());
                session.update(updated);
                detachedEntityRelation.unsafeAddRelation(updated);
            }
        }

        //Per tutti gli U rimossi(che sono presi da attachedEntity, quindi sono già consistenti sul DB mi limito a rimuovere la relazione ed aggiornarli)
        for (U rem : removed)
        {
            attachedEntity.getInverse(rem).setRelation(null);
            session.update(rem);
        }
    }

    /**
     * Metodo generico che aggiorna la relazione MoltiAMolti di un oggetto owner
     * @param detachedEntityRelation Relazione tra T Detached(Modificato) e U (Detached)
     * @param ownedClass Classe dell'oggetto della relazione Owned
     * @param session Sessione attuale del DataBase
     * @param <T> Tipo dell'oggetto Owned
     * @param <U> Tipo dell'oggetto Owner
     */
    public static <T extends TransferableEntity, U extends TransferableEntity>
        void updateManyToManyOwner(IManyToManyOwner<U,T> detachedEntityRelation, Class<U> ownedClass, DatabaseSession.DatabaseSessionInstance session)
    {
        // Dato che sono owner devo preoccuparmi che gli U che andrò a salvare devono essere consistenti con quelli sul DB,
        // essendo owner non mi preoccupo di modificare la relazione per gli U rimossi dato che sarà aggiornata automaticamente
        Set<U> detachedSet = detachedEntityRelation.getUnmodifiableRelation();
        for (U unupdated : detachedSet)
        {
            detachedEntityRelation.removeRelation(unupdated); //Rimuovo l'istanza con i dati vecchi di U
            U updated = session.getByID(ownedClass, unupdated.getID()); //Ottengo i dati nuovi
            if (updated != null) //Se i dati nuovi esistono (U non è stato cancellato nel frattempo)
                detachedEntityRelation.addRelation(updated); //Riaggiungo U con i dati nuovi
            //Noto che se un U è stato tolto dalla lista, la relazione verrà aggiornata automaticamente da Hibernate dato che sono l'owner
        }
    }

    /**
     * Metodo generico che date 2 istanze delle stesso oggetto (una detached ma modificata, una attached che rappresenta quell'oggetto nella
     * sessione attuale sul DB) effettua l'update corretto della relazione specificata
     * @param detachedEntityRelation Relazione tra T Detached(Modificato) e U (Detached)
     * @param attachedEntity Relazione tra T Attached (Dati vecchi) e U (Attached)
     * @param ownerClass Classe dell'oggetto della relazione Owner
     * @param session Sessione attuale del DataBase
     * @param <T> Tipo dell'oggetto Owned
     * @param <U> Tipo dell'oggetto Owner
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
                detachedEntityRelation.getInverse(updated).addRelation(detachedEntityRelation.getItem());
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
