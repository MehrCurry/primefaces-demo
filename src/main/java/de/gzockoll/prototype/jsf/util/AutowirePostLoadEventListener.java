package de.gzockoll.prototype.jsf.util;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AutowirePostLoadEventListener implements PostLoadEventListener {

    @Autowired
    private ApplicationContext context;

    @Override
    public void onPostLoad(PostLoadEvent postLoadEvent) {
        Object entity = postLoadEvent.getEntity();

        AutowireCapableBeanFactory spring = context.getAutowireCapableBeanFactory();
        spring.autowireBean(entity);
    }
}
