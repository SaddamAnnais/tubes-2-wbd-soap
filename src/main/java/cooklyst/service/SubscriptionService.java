package cooklyst.service;

import cooklyst.entity.Subscription;
import cooklyst.repository.SubscriptionRepository;
import cooklyst.util.Env;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@HandlerChain(file = "../middleware/handlers.xml")
public class SubscriptionService {
    private static final SubscriptionRepository subsRepo = new SubscriptionRepository();

    @WebMethod
    public String create(String phpKey, int creatorId, int subscriberID, String subscriberEmail) {
        if (!phpKey.equals(Env.PHP_KEY)) {
            return "Unauthorized";
        }
        return subsRepo.create(creatorId, subscriberID, subscriberEmail);
    }

    @WebMethod
    public String approve(String restKey, int creatorId, int subscriberID) {
        if (!restKey.equals(Env.REST_KEY)) {
            return "Unauthorized";
        }
        return subsRepo.approve(creatorId, subscriberID);
    }

    @WebMethod
    public String reject(String restKey, int creatorId, int subscriberID) {
        if (!restKey.equals(Env.REST_KEY)) {
            return "Unauthorized";
        }
        return subsRepo.reject(creatorId, subscriberID);
    }

    @WebMethod
    public List<Subscription> getPendingSubs(String restKey) {
        if (!restKey.equals(Env.REST_KEY)) {
            return new ArrayList<>();
        }
        return subsRepo.getPendingSubs();
    }

    @WebMethod
    public String getStatus(String restKey, int creatorId, int subscriberID) {
        if (!restKey.equals(Env.REST_KEY)) {
            return "Unauthorized";
        }
        System.out.println("babi soap anj");
        return subsRepo.getStatus(creatorId, subscriberID).toString();
    }
}
