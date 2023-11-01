package cooklyst.services;

import cooklyst.models.Subscription;
import cooklyst.utils.Env;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class SubscriptionService {
    private static final SubscriptionController subsCon = new SubscriptionController();

    @WebMethod
    public String create(String phpKey, int creatorId, int subscriberId) {
        if (!phpKey.equals(Env.PHP_KEY)) {
            return "Unauthorized";
        }
        return subsCon.create(creatorId, subscriberId);
    }

    @WebMethod
    public String approve(String restKey, int creatorId, int subscriberId) {
        if (!restKey.equals(Env.REST_KEY)) {
            return "Unauthorized";
        }
        return subsCon.approve(creatorId, subscriberId);
    }

    @WebMethod
    public String reject(String restKey, int creatorId, int subscriberId) {
        if (!restKey.equals(Env.REST_KEY)) {
            return "Unauthorized";
        }
        return subsCon.approve(creatorId, subscriberId);
    }

    @WebMethod
    public List<Subscription> getPendingSubs(String restKey) {
        if (!restKey.equals(Env.REST_KEY)) {
            return new ArrayList<>();
        }
        return subsCon.getPendingSubs();
    }
}
