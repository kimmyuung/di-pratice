package org.example.di;

import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.Set;

public class BeanFactoryUtil {
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectConstructors = ReflectionUtils.getConstructors(
                clazz, ReflectionUtils.withAnnotation(Inject.class));

        if(injectConstructors.isEmpty()) {
            return null;
        }
        return injectConstructors.iterator().next();
    }

}

