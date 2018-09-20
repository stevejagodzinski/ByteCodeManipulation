package com.github.stevejagodzinski.bcm.clazz;

public class ClassNameConverter {
    private ClassNameConverter() {
        throw new UnsupportedOperationException("Static class should not be instantiated");
    }

    public static String toDotSeparated(String className) {
        return className.replaceAll("/", ".");
    }

    public static String toSlashSeparated(String className) {
        return className.replaceAll("\\.", "/");
    }
}
