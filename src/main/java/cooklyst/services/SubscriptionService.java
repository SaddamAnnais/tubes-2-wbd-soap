package cooklyst.services;

import cooklyst.utils.Env;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class SubscriptionService {
    @WebMethod
    public String hello(String soapKey, String name) {
        if (!soapKey.equals(Env.SOAP_KEY)) {
            return "Unauthorized";
        }
        return "Hello " + name;
    }
}
