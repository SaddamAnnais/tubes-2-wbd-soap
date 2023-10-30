package cooklyst.tubes2wbdsoap.utils;

import cooklyst.tubes2wbdsoap.models.Subscription;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class Hibernate {
    private static final String DB_HOST = "db-soap";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "cooklyst_soap";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    private static final String DB_URL = String.format("jdbc:mysql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);

    private static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        properties.setProperty("hibernate.connection.url", Hibernate.DB_URL);
        properties.setProperty("hibernate.connection.username", Hibernate.DB_USER);
        properties.setProperty("hibernate.connection.password", Hibernate.DB_PASS);
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.current_session_context_class", "thread");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Subscription.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        if (Hibernate.sessionFactory == null) {
            Hibernate.sessionFactory = Hibernate.buildSessionFactory();
        }
        return Hibernate.sessionFactory;
    }
}
