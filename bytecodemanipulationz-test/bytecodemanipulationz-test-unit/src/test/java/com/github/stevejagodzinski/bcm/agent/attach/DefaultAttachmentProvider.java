package com.github.stevejagodzinski.bcm.agent.attach;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class DefaultAttachmentProvider implements AttachmentProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAttachmentProvider.class);

    public static final AttachmentProvider INSTANCE = new DefaultAttachmentProvider();

    private DefaultAttachmentProvider() {}

    public void attach(String processId, String arguments) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach(processId);
        String path = AgentJarPathProvider.PATH;
        loadAgent(path, vm);
        vm.detach();
    }

    private void loadAgent(String agentJarPath, VirtualMachine virtualMachine) throws IOException, AgentLoadException, AgentInitializationException {
        Objects.requireNonNull(virtualMachine, "virtualMachine must not be null");
        Objects.requireNonNull(agentJarPath, "agentJarPath must not be null");
        LOG.debug("Loading agent from {}", agentJarPath);
        virtualMachine.loadAgent(agentJarPath);
    }
}
