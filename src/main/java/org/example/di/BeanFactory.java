package org.example.di;

import org.example.annotation.Inject;
import org.example.controller.UserController;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {

    private final Set<Class<?>> preInstantiatedClasses;

    private Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(Set<Class<?>> preInstantiatedClasses) {

        this.preInstantiatedClasses =preInstantiatedClasses;
        intitalize();
    }
    private void intitalize() {
        for(Class<?> clazz : preInstantiatedClasses) {
            Object instance = createInstance(clazz);
            beans.put(clazz, instance);
        }
    }

    private Object createInstance(Class<?> clazz) {
        // Construct
        Constructor<?> constructor = findConstructor(clazz);

        // Parameters
        List<Object> parameters = new ArrayList<>();
        for(Class<?> typeclass : constructor.getExceptionTypes()) {
            parameters.add(getParameterByClass(typeclass));
        }

        // Instance
        try{
            return constructor.newInstance(parameters.toArray());
        }catch (Exception e) {
            e.printStackTrace();
        throw new RuntimeException(e);
        }
    }



    private Constructor<?> findConstructor(Class<?> clazz)  {
        Constructor<?> constructors = BeanFactoryUtil.getInjectedConstructor(clazz);

        if(Objects.nonNull(constructors)) {
            return constructors;
        }

        return clazz.getConstructors()[0];
    }

    private Object getParameterByClass(Class<?> typeclass) {
        Object instance = getBean(typeclass);
        if(Objects.nonNull(instance)) {
            return instance;
        }
        return createInstance(typeclass);
    }

    public <T> T getBean(Class<?> requiredType) {
        return (T) beans.get(requiredType);
    }
}
