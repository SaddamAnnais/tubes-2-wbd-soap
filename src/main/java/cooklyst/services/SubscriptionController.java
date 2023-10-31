package cooklyst.services;

import cooklyst.utils.SubsStatus;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import cooklyst.models.Subscription;
import cooklyst.utils.Hibernate;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SubscriptionController {
    public String create(int creatorId, int subscriberId) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toCreate = new Subscription();
            toCreate.setCreatorID(creatorId);
            toCreate.setSubscriberID(subscriberId);
            session.save(toCreate);
            session.getTransaction().commit();

            return "Subscription created";
        } catch (Exception e) {
            return "An error occurred";
        }
    }

    public List<Subscription> getPendingSubs() {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Subscription> criteria = builder.createQuery(Subscription.class);
            Root<Subscription> root = criteria.from(Subscription.class);
            Predicate predicate = builder.equal(root.get("status"), SubsStatus.PENDING);
            criteria.select(root).where(predicate);
            TypedQuery<Subscription> query = session.createQuery(criteria);
            List<Subscription> subscriptions = query.getResultList();
            session.getTransaction().commit();

            return subscriptions;

        } catch (Exception e) {
            return null;
        }
    }

    public SubsStatus getStatus(int creatorId, int subscriberId) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toFind = new Subscription();
            toFind.setCreatorID(creatorId);
            toFind.setSubscriberID(subscriberId);
            Subscription subscription = session.get(Subscription.class, toFind);
            session.getTransaction().commit();

            if (subscription == null) {
                return null;
            }

            return subscription.getStatus();

        } catch (Exception e) {
            return null;
        }
    }

    public String approve(int creatorId, int subscriberId) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toFind = new Subscription();
            toFind.setCreatorID(creatorId);
            toFind.setSubscriberID(subscriberId);
            Subscription toApprove = session.get(Subscription.class, toFind);

            if (toApprove == null) {
                session.getTransaction().commit();
                return "No subscription request found";
            }

            switch (toApprove.getStatus()) {
                case PENDING:
                    toApprove.setStatus(SubsStatus.APPROVED);
                    session.update(toApprove);
                    session.getTransaction().commit();
                    return "Successfully approved subscription request";
                case APPROVED:
                    session.getTransaction().commit();
                    return "Subscription request already approved";
                default:
                    session.getTransaction().commit();
                    return "Subscription request already rejected";
            }

        } catch (Exception e) {
            return "An error occurred";
        }
    }

    public String reject(int creatorId, int subscriberId) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toFind = new Subscription();
            toFind.setCreatorID(creatorId);
            toFind.setSubscriberID(subscriberId);
            Subscription toReject = session.get(Subscription.class, toFind);

            if (toReject == null) {
                session.getTransaction().commit();
                return "No subscription request found";
            }

            switch (toReject.getStatus()) {
                case PENDING:
                    toReject.setStatus(SubsStatus.APPROVED);
                    session.update(toReject);
                    session.getTransaction().commit();
                    return "Successfully rejected subscription request";
                case APPROVED:
                    session.getTransaction().commit();
                    return "Subscription request already approved";
                default:
                    session.getTransaction().commit();
                    return "Subscription request already rejected";
            }

        } catch (Exception e) {
            return "An error occurred";
        }
    }
}
