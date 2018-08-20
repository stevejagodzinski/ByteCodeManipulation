package com.github.stevejagodzinski.bcm.agent.attach;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AgentInstaller {

    private static final Logger LOG = LoggerFactory.getLogger(AgentInstaller.class);

    public static final AgentInstaller INSTANCE = new AgentInstaller();

    private String AGENT_ATTACH_ARGUMENTS = null;

    private AgentInstaller() {}

    public void installAgent() throws AgentInitializationException, AgentLoadException, AttachNotSupportedException, IOException {
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
