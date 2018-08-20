package com.github.stevejagodzinski.bcm.agent.attach;

import com.github.stevejagodzinski.bcm.JavaAgent;

import java.net.URL;

public class AgentJarPathProvider {
    public static String path() {
        URL classUrl = JavaAgent.class.getResource("JavaAgent.class");
        String classFilePath = classUrl.getFile();
        String[] split = classFilePath.split("!");
        if (split.length != 2) {
            throw new IllegalStateException("Can not determine jar file containing java agent");
        }
        return split[0];
    }
}
