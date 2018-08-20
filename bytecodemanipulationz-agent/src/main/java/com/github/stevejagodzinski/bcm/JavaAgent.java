package com.github.stevejagodzinski.bcm;

import com.github.stevejagodzinski.bcm.transformers.HttpRequestTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class JavaAgent {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAgent.class);

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        LOG.info("Starting the agent from the command line.");
        install(agentArgs, instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        LOG.info("Agent stated after VM startup.");
        install(agentArgs, instrumentation);
    }

    private static void install(String agentArgs, Instrumentation instrumentation) {
        LOG.info("Starting the agent with arguments {}", agentArgs);
        instrumentation.addTransformer(new HttpRequestTransformer());
    }
}
