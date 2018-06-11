package com.polimi.childcare.server.database.dao.implementations;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.database.dao.HibernateDao;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegistroPresenzeDaoImpl extends HibernateDao<RegistroPresenze>
{
    public RegistroPresenzeDaoImpl(DatabaseSession.DatabaseSessionInstance sessionInstance) { super(sessionInstance); }

    @Override
    public void delete(RegistroPresenze item)
    {
        sessionInstance.delete(item);
    }

    @Override
    public int insert(RegistroPresenze item)
    {
        checkConstraints(item);
        int ID = sessionInstance.insert(item);
        DBHelper.updateManyToOne(item.asRegistroPresenzeBambinoRelation(), Bambino.class, sessionInstance);
        DBHelper.updateManyToOne(item.asRegistroPresenzeGitaRelation(), Gita.class, sessionInstance);
        return ID;
    }

    @Override
    public void update(RegistroPresenze item)
    {
        checkConstraints(item);
        RegistroPresenze dbEntity = sessionInstance.getByID(RegistroPresenze.class, item.getID());

        if(dbEntity != null)
        {
            DBHelper.updateManyToOne(item.asRegistroPresenzeBambinoRelation(), Bambino.class, sessionInstance);
            DBHelper.updateManyToOne(item.asRegistroPresenzeGitaRelation(), Gita.class, sessionInstance);
            sessionInstance.insertOrUpdate(item);
        }
        else
            insert(item);
    }

    private void checkConstraints(RegistroPresenze gruppo)
    {
        if (gruppo.getDate() == null || gruppo.getTimeStamp() == null || gruppo.getStato() == null)
            throw new RuntimeException("Campi obbligatori vuoti!");
    }

    /**
         Query eseguita:

         SELECT *
         FROM RegistroPresenze
         WHERE (Bambino_FK, TimeStamp) IN (
             SELECT Bambino_FK, MAX(TimeStamp) as `TimeStamp`
             FROM RegistroPresenze
             WHERE TimeStamp > DateTime.Date.Now
             GROUP BY Bambino_FK
         )
        Ritorna le ultime presenze distinte per bambino
     * @return Lista contenente l'ultimo stato presenza per ogni bambino
     */
    public List<RegistroPresenze> getLastPresenze()
    {
        List<RegistroPresenze> result = new ArrayList<>();
        CriteriaBuilder queryBuilder = sessionInstance.getSession().getCriteriaBuilder();

        CriteriaQuery<Object[]> subQuery = queryBuilder.createQuery(Object[].class);
        Root<RegistroPresenze> registroPresenzeRoot = subQuery.from(RegistroPresenze.class);
        subQuery.multiselect(
                registroPresenzeRoot.get("bambino"),
                queryBuilder.max(registroPresenzeRoot.get("TimeStamp"))
        );
        subQuery.where(queryBuilder.greaterThanOrEqualTo(registroPresenzeRoot.get("TimeStamp"), LocalDateTime.now().toLocalDate().atStartOfDay()));
        subQuery.groupBy(registroPresenzeRoot.get("bambino"));
        Iterator<Object[]> objects = sessionInstance.getSession().createQuery(subQuery).getResultStream().iterator();

        while(objects.hasNext())
        {
            Object[] tuple = objects.next();

            CriteriaQuery<RegistroPresenze> criteriaSelectIn = queryBuilder.createQuery(RegistroPresenze.class);
            Root<RegistroPresenze> criteriaSelectFrom = criteriaSelectIn.from(RegistroPresenze.class);
            Path<Bambino> bambino = criteriaSelectFrom.get("bambino");
            Path<LocalDateTime> timestamp = criteriaSelectFrom.get("TimeStamp");
            criteriaSelectIn.where(queryBuilder.equal(bambino, tuple[0]), queryBuilder.equal(timestamp, tuple[1]));
            CollectionUtils.addAll(result, sessionInstance.getSession().createQuery(criteriaSelectIn).getResultStream().iterator());
        }

        return result;
    }
}
