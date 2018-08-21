package com.github.stevejagodzinski.bcm.agent.attach;

import java.lang.management.ManagementFactory;

public class DefaultProcessIdProvider implements ProcessIdProvider {

    public static final ProcessIdProvider INSTANCE = new DefaultProcessIdProvider();

    private DefaultProcessIdProvider() {}

    public String getProcessId() {
        String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
        int processIdIndex = runtimeName.indexOf('@');
        if (processIdIndex == -1) {
            throw new IllegalStateException("Cannot extract process id from runtime management bean");
        } else {
            return runtimeName.substring(0, processIdIndex);
        }
    }
}
