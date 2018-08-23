package com.github.stevejagodzinski.bcm.agent.attach;

public interface AttachmentProvider {
    void attach(String processId, String arguments) throws AttachException;
}