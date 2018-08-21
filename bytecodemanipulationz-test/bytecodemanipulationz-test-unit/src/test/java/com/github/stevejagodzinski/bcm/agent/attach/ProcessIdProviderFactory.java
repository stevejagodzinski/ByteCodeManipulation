package com.github.stevejagodzinski.bcm.agent.attach;

public class ProcessIdProviderFactory {

    public static final ProcessIdProviderFactory INSTANCE = new ProcessIdProviderFactory();

    private static final ProcessIdProvider DEFAULT = DefaultProcessIdProvider.INSTANCE;

    private ProcessIdProviderFactory() {}

    public ProcessIdProvider getProcessIdProvider() {
        return DEFAULT;
    }
}
