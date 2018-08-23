package com.github.stevejagodzinski.bcm.agent.attach;

import com.github.stevejagodzinski.bcm.JavaAgent;

import java.net.URL;
import java.util.Optional;

public class AgentJarPathProvider {

    private static final String AGENT_JAR_PATH_ENVIRONMENT_VARIABLE = "com.github.stevejagodzinski.bcm.agent.attach.jar";

    private static final String OS = System.getProperty("os.name");
    private static final String FILE_URL_PREFIX = OS.startsWith("Windows") ? "file:/" : "file:";
    private static final int FILE_URL_PREFIX_LENGTH = FILE_URL_PREFIX.length();

    private AgentJarPathProvider() {
        throw new UnsupportedOperationException("Class with only static utility methods should not be instantiated");
    }

    public static final String PATH = path();

    private static String path() {
        return Optional.ofNullable(findJarProvidedAsEnvironmentVariable()).orElseGet(AgentJarPathProvider::findJarOnClasspath);
    }

    // Allows developer to set the JAR file path as an environment variable to run the unit tests in the IDE
    private static String findJarProvidedAsEnvironmentVariable() {
        return System.getenv(AGENT_JAR_PATH_ENVIRONMENT_VARIABLE);
    }

    private static String findJarOnClasspath() {
        URL classUrl = JavaAgent.class.getResource("JavaAgent.class");
        String classFilePath = classUrl.getFile();
        String[] split = classFilePath.split("!");

        if (split.length != 2) {
            throw new IllegalStateException("Can not determine jar file containing java agent: classFilePath=" + classFilePath);
        }
        String jarUrl = split[0];


        if (!jarUrl.startsWith(FILE_URL_PREFIX)) {
            throw new UnsupportedOperationException("Can not determine the path of a jar that does not reside on the file system");
        }

        return jarUrl.substring(FILE_URL_PREFIX_LENGTH);
    }
}
