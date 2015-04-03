package de.gzockoll.prototype.jsf.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public abstract class AutowiringEntityFactory<T> {

    @Autowired
    protected AutowireCapableBeanFactory factory;

    public T createInstance() {
        T instance=createNewInstance();
        factory.autowireBean(instance);
        return instance;
    }

    protected abstract T createNewInstance();
}
