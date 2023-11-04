package cooklyst.middleware;

import java.util.Date;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.xml.ws.developer.JAXWSProperties;

import cooklyst.models.Log;
import cooklyst.utils.Hibernate;

public class Logger implements SOAPHandler<SOAPMessageContext> {
  private void logToDB(SOAPMessageContext smc) throws SOAPException {
    boolean isResponse = (boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    HttpExchange http = (HttpExchange) smc.get(JAXWSProperties.HTTP_EXCHANGE);

    if (!isResponse) {
      SOAPPart part = smc.getMessage().getSOAPPart();
      SOAPEnvelope envelope = part.getEnvelope();
      SOAPBody body = envelope.getBody();

      Node operation = body.getChildNodes().item(1);

      // getting the operation name
      String content = String.format("%s", operation.getLocalName());

      NodeList parameters = operation.getChildNodes();
      for (int i = 1; i < parameters.getLength(); i += 2) {
        // getting the parameter name and value
        content = String.format("%s %s(%s)", content, parameters.item(i).getLocalName(),
            parameters.item(i).getTextContent());
      }

      // logging
      Log newLog = new Log();
      newLog.setDescription(content);
      newLog.setEndpoint("test endpoint");


      // ERROR
      // HTTP Exchange does not work idk why and it does not shows any error message whatsoever. 
      
      // newLog.setEndpoint(http.getRequestURI().getPath());
      newLog.setEndpoint("Test endpoint");

      // newLog.setIPAddress(http.getRemoteAddress().getHostString());
      newLog.setIPAddress("Test IP ADDRESS");
      newLog.setTimestamp(new Date());
      SessionFactory sessionFactory = Hibernate.getSessionFactory();
      Session session = sessionFactory.getCurrentSession();

      session.beginTransaction();
      session.save(newLog);
      session.getTransaction().commit();
    }
  }

  public Set<QName> getHeaders() {
    return null;
  }

  public boolean handleMessage(SOAPMessageContext smc) {
    try {
      logToDB(smc);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean handleFault(SOAPMessageContext smc) {
    try {
      logToDB(smc);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public void close(MessageContext messageContext) {
    // PASS
  }
}