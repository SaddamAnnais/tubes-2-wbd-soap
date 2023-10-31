package cooklyst.services;

import cooklyst.utils.SubsStatus;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import cooklyst.models.Subscription;
import cooklyst.utils.Hibernate;

public class SubscriptionController {
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
