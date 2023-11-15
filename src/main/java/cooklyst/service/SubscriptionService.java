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
    private static final SubscriptionRepository subsCon = new SubscriptionRepository();

    @WebMethod
    public String create(String phpKey, int creatorId, String subscriberEmail) {
        if (!phpKey.equals(Env.PHP_KEY)) {
            return "Unauthorized";
        }
        return subsCon.create(creatorId, subscriberEmail);
    }

    @WebMethod
    public String approve(String restKey, int creatorId, String subscriberEmail) {
        if (!restKey.equals(Env.REST_KEY)) {
            return "Unauthorized";
        }
        return subsCon.approve(creatorId, subscriberEmail);
    }

    @WebMethod
    public String reject(String restKey, int creatorId, String subscriberEmail) {
        if (!restKey.equals(Env.REST_KEY)) {
            return "Unauthorized";
        }
        return subsCon.reject(creatorId, subscriberEmail);
    }

    @WebMethod
    public List<Subscription> getPendingSubs(String restKey) {
        if (!restKey.equals(Env.REST_KEY)) {
            return new ArrayList<>();
        }
        return subsCon.getPendingSubs();
    }
}
