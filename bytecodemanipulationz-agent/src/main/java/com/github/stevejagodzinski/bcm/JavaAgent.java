package com.github.stevejagodzinski.bcm;

import com.github.stevejagodzinski.bcm.transformers.HttpRequestTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class JavaAgent {
    private static final Logger LOG = LoggerFactory.getLogger(JavaAgent.class);

    private JavaAgent() {
        throw new UnsupportedOperationException("Static premain/agentmain class should not be instantiated");
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        LOG.info("Starting the agent from the command line.");
        install(agentArgs, instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        LOG.info("Agent stated after VM startup.");
        if (instrumentation.isRetransformClassesSupported()) {
            install(agentArgs, instrumentation);
            reTransformLoadedClasses(instrumentation);
        } else {
            throw new UnsupportedOperationException("retransformClasses is not supported");
        }
    }

    private static void reTransformLoadedClasses(Instrumentation instrumentation) {
        for (Class clazz : instrumentation.getAllLoadedClasses()) {
            if (isHttpServlet(clazz)) {
                try {
                    LOG.info("Re-transforming class {}", clazz);
                    instrumentation.retransformClasses(clazz);
                } catch (Exception e) {
                    LOG.error("Could not re-transform class {}", clazz, e);
                }
            } else {
                LOG.debug("Not re-transforming class {}", clazz);
            }
        }
    }

    private static boolean isHttpServlet(Class clazz) {
        return clazz.getName().equals(HttpRequestTransformer.TARGET_CLASS_DOT);
    }

    private static void install(String agentArgs, Instrumentation instrumentation) {
        LOG.info("Starting the agent with arguments {}", agentArgs);
        instrumentation.addTransformer(new HttpRequestTransformer(), true);
    }
}
