package com.github.stevejagodzinski.bcm.clazz;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassNameConverterTest {
    @Test
    public void testToDotSeparated() {
        // Given
        String slashSeparatedClassName = "javax/servlet/http/HttpServlet";

        // When
        String dotSeparatedClassName = ClassNameConverter.toDotSeparated(slashSeparatedClassName);

        // Then
        String expected = "javax.servlet.http.HttpServlet";
        assertEquals(expected, dotSeparatedClassName);
    }

    @Test
    public void testToSlashSeparated() {
        // Given
        String dotSeparatedClassName = "javax.servlet.http.HttpServlet";

        // When
        String slashSeparatedClassName = ClassNameConverter.toSlashSeparated(dotSeparatedClassName);

        // Then
        String expected = "javax/servlet/http/HttpServlet";
        assertEquals(expected, slashSeparatedClassName);
    }
}
