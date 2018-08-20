package com.github.stevejagodzinski.bcm.agent.attach;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;

import java.io.IOException;

public interface AttachmentProvider {
    void attach(String processId, String arguments) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException;
}