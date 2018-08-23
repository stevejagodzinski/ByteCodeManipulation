package com.github.stevejagodzinski.bcm.agent.attach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentInstaller {

    private static final Logger LOG = LoggerFactory.getLogger(AgentInstaller.class);

    public static final AgentInstaller INSTANCE = new AgentInstaller();

    private static final String AGENT_ATTACH_ARGUMENTS = null;

    private AgentInstaller() {}

    public void installAgent() throws AttachException {
        LOG.info("Installing Agent");
        String processId = getProcessId();
        AttachmentProvider attachmentProvider = getAttachmentProvider();
        attachmentProvider.attach(processId, AGENT_ATTACH_ARGUMENTS);
    }

    private String getProcessId() {
        return ProcessIdProviderFactory.INSTANCE.getProcessIdProvider().getProcessId();
    }

    private AttachmentProvider getAttachmentProvider() {
        return AttachmentProviderFactory.INSTANCE.getAttachmentProvider();
    }
}
