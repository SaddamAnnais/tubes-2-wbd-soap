package cooklyst.util;

import cooklyst.entity.Log;
import cooklyst.entity.Subscription;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class Hibernate {
    private static final String DB_URL = String.format("jdbc:mysql://%s:%s/%s", Env.DB_HOST, Env.DB_PORT, Env.DB_NAME);

    private static SessionFactory sessionFactory;

    private static SessionFactory createSessionFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        properties.setProperty("hibernate.connection.url", Hibernate.DB_URL);
        properties.setProperty("hibernate.connection.username", Env.DB_USER);
        properties.setProperty("hibernate.connection.password", Env.DB_PASS);
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.current_session_context_class", "thread");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Log.class);
        configuration.addAnnotatedClass(Subscription.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        if (Hibernate.sessionFactory == null) {
            Hibernate.sessionFactory = Hibernate.createSessionFactory();
        }
        return Hibernate.sessionFactory;
    }
}
