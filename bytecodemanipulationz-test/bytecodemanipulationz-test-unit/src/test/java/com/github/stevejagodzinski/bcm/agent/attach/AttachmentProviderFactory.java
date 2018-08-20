package com.github.stevejagodzinski.bcm.agent.attach;

public class AttachmentProviderFactory {

    public static final AttachmentProviderFactory INSTANCE = new AttachmentProviderFactory();

    private static final AttachmentProvider DEFAULT = DefaultAttachmentProvider.INSTANCE;

    private AttachmentProviderFactory() {}

    public AttachmentProvider getAttachmentProvider() {
        return DEFAULT;
    }
}
