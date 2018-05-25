package com.polimi.childcare.server.helper.query;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe helper per eseguire query "speciali" per la gestione delle presenze
 */
public class RegistroPresenzeQuery
{
    /**
     * Ritorna una lista di eventi delle presenze utilizzabile per calcolare nuovi stati per un evento avvenuto ad un istante utcEpochSeconds
     * @param utcEpochSeconds Istante in cui l'evento è avvenuto
     * @param bambinoId ID del bambino
     * @return Lista con list[0] evento prima dell'istante (può non esserci) ed il resto degli elementi con timestamp > utcEpochSeconds ordinati
     */
    public static ArrayList<RegistroPresenze> getStatoPresenzeAtEpochSeconds(long utcEpochSeconds, int bambinoId)
    {
        final ArrayList<RegistroPresenze> ultimiStatiPresenze = new ArrayList<>();
        DatabaseSession.getInstance().execute((context) ->
        {
            LocalDateTime dateTimeEvent = LocalDateTime.ofEpochSecond(utcEpochSeconds, 0, ZoneOffset.UTC);
            CriteriaBuilder criteriaBuilder = context.getSession().getCriteriaBuilder();
            CriteriaQuery<RegistroPresenze> query = criteriaBuilder.createQuery(RegistroPresenze.class);
            Root<RegistroPresenze> root = query.from(RegistroPresenze.class);
            query.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("bambino"), bambinoId),
                    criteriaBuilder.greaterThan(root.get("TimeStamp"), dateTimeEvent)))
                    .orderBy(criteriaBuilder.asc(root.get("TimeStamp")));
            CollectionUtils.addAll(ultimiStatiPresenze, context.getSession().createQuery(query).iterate());

            criteriaBuilder = context.getSession().getCriteriaBuilder();
            query = criteriaBuilder.createQuery(RegistroPresenze.class);
            root = query.from(RegistroPresenze.class);
            query.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("bambino"), bambinoId),
                    criteriaBuilder.lessThanOrEqualTo(root.get("TimeStamp"), dateTimeEvent)))
                    .orderBy(criteriaBuilder.desc(root.get("TimeStamp")));
            Iterator<RegistroPresenze> presenzeIterator = context.getSession().createQuery(query).iterate();

            if(presenzeIterator.hasNext())
                ultimiStatiPresenze.add(0, presenzeIterator.next());

            return true;
        });
        return ultimiStatiPresenze;
    }
}
