package com.swlc.social_media.utill;

import com.swlc.social_media.entity.ChannelEntity;
import com.swlc.social_media.entity.PostEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.IOException;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.setProperties(properties);

        configuration.addAnnotatedClass(ChannelEntity.class).addAnnotatedClass(PostEntity.class);
        sessionFactory = configuration.buildSessionFactory();
    }
    public static FactoryConfiguration getFactoryConfiguration(){
        return factoryConfiguration == null ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }
    public Session getSession(){
        return sessionFactory.openSession();
    }
}
