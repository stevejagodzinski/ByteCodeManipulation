package com.github.stevejagodzinski.bcm.classloader;

import com.github.stevejagodzinski.bcm.clazz.ClassNameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaderService {
    private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderService.class);

    private ClassLoaderService() {
        throw new UnsupportedOperationException("Static class should not be instantiated");
    }

    public static ClassLoader getClassLoader(String className) throws ClassNotFoundException {
        String dotSeparated = ClassNameConverter.toDotSeparated(className);
        Class clazz = Class.forName(dotSeparated);
        ClassLoader classLoader = clazz.getClassLoader();

        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        LOG.debug("Class {} loaded with {}", className, classLoader);

        return classLoader;
    }

    public static ClassLoader getCurrentContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
