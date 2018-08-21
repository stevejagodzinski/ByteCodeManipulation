package com.github.stevejagodzinski.bcm.agent.attach;

import com.github.stevejagodzinski.bcm.JavaAgent;

import java.net.URL;

public class AgentJarPathProvider {

    private static final String FILE_URL_PREFIX = "file:/";
    private static final int FILE_URL_PREFIX_LENGTH = FILE_URL_PREFIX.length();

    public static final String PATH = path();

    private static String path() {
        URL classUrl = JavaAgent.class.getResource("JavaAgent.class");
        String classFilePath = classUrl.getFile();
        String[] split = classFilePath.split("!");

        if (split.length != 2) {
            throw new IllegalStateException("Can not determine jar file containing java agent");
        }
        String jarUrl = split[0];


        if (!jarUrl.startsWith(FILE_URL_PREFIX)) {
            throw new UnsupportedOperationException("Can not determine the path of a jar that does not reside on the file system");
        }

        return jarUrl.substring(FILE_URL_PREFIX_LENGTH);
    }
}
