package cooklyst.repository;

import cooklyst.util.EmailSender;
import cooklyst.util.SubsStatus;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import cooklyst.entity.Subscription;
import cooklyst.util.Hibernate;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepository {
    public String create(int creatorId, int subscriberID, String subscriberEmail) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toCreate = new Subscription();
            toCreate.setCreatorID(creatorId);
            toCreate.setSubscriberID(subscriberID);
            toCreate.setSubscriberEmail(subscriberEmail);
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
            return new ArrayList<Subscription>();
        }
    }

    public SubsStatus getStatus(int creatorId, int subscriberID) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toFind = new Subscription();
            toFind.setCreatorID(creatorId);
            toFind.setSubscriberID(subscriberID);
            Subscription subscription = session.get(Subscription.class, toFind);
            session.getTransaction().commit();

            if (subscription == null) {
                return SubsStatus.NO_DATA;
            }

            return subscription.getStatus();

        } catch (Exception e) {
            return SubsStatus.NO_DATA;
        }
    }

    public String approve(int creatorId, int subscriberID) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toFind = new Subscription();
            toFind.setCreatorID(creatorId);
            toFind.setSubscriberID(subscriberID);
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

                    EmailSender e = new EmailSender();
                    e.send(toApprove.getSubscriberEmail(), true);

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

    public String reject(int creatorId, int subscriberID) {
        try {
            SessionFactory sessionFactory = Hibernate.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            session.beginTransaction();
            Subscription toFind = new Subscription();
            toFind.setCreatorID(creatorId);
            toFind.setSubscriberID(subscriberID);
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

                    EmailSender e = new EmailSender();
                    e.send(toReject.getSubscriberEmail(), false);

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
