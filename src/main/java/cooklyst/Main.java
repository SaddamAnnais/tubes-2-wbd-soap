package cooklyst;

import cooklyst.services.SubscriptionService;
import cooklyst.utils.Hibernate;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        Hibernate.getSessionFactory();

        Endpoint endpoint = Endpoint.create(new SubscriptionService());

        // kalo pake war intellij
//        endpoint.publish("http://localhost:8001/tubes_2_wbd_soap_war_exploded/api/subscribe");

        // kalo pake maven
         endpoint.publish("http://localhost:8001/api/subscribe");
    }
}
