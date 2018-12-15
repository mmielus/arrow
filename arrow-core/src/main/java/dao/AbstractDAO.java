package dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

import lombok.Getter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@Getter
public abstract class AbstractDAO
{
   //  private static final Logger logger = LoggerFactory.getLogger(AbstractDAO.class);

   @PersistenceContext
   private EntityManager entityManager;

   private Session session;

   public <ENTITY extends Serializable> void save(ENTITY object)
   {
      entityManager.persist(object);

      //  logger.info("{}saved", object.getClass());
   }

   public <ENTITY extends Serializable> ENTITY getObjectById(Class<ENTITY> clazz, int id)
   {
      return entityManager.find(clazz, id);
   }

   protected <ENTITY extends Serializable> List<ENTITY> search(Criteria criteria)
   {
      return criteria.list();
   }

   protected CriteriaBuilder getCriteriaBuilder()
   {
      return entityManager.getCriteriaBuilder();
   }

   public abstract static class AbstractQueryApplier<ENTITY extends Serializable>
   {

      protected final Criteria criteria;

      protected AbstractQueryApplier(Session session, Class<ENTITY> clazz)
      {
         criteria = session.createCriteria(clazz);

      }

      protected void addRestriction(String parameter, String value, String operator)
      {
         switch (operator)
         {
            case "like":
               criteria.add(Restrictions.like(parameter, value));
         }
      }

      public Criteria buildCriteriaQuery()
      {
         return criteria;
      }
   }

}
