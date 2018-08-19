package com.github.stevejagodzinski.bcm;

import com.github.stevejagodzinski.bcm.transformers.HttpRequestTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class JavaAgent {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAgent.class);

    public static void premain(String agentArgs, Instrumentation inst) {
        LOG.info("Starting the agent with arguments {}", agentArgs);
        inst.addTransformer(new HttpRequestTransformer());
    }
}
