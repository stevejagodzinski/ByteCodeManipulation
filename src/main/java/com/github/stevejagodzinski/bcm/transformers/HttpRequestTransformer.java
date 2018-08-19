package com.github.stevejagodzinski.bcm.transformers;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class HttpRequestTransformer implements ClassFileTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestTransformer.class);

    private static final String TARGET_CLASS_DOT = HttpServlet.class.getCanonicalName();
    private static final String TARGET_CLASS_SLASH = TARGET_CLASS_DOT.replaceAll("\\.", "/");

    private final ClassPool pool = ClassPool.getDefault();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] transformed = null;
        try {
            pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));

            if (className.equals(TARGET_CLASS_SLASH)) {
                CtClass ctClass = pool.get(TARGET_CLASS_DOT);
                if (!ctClass.isFrozen()) {
                    // TODO: Add specific transformations to the ctClass
                    transformed = ctClass.toBytecode();
                } else {
                    LOG.error("Class has already been loaded or written out and thus it cannot be modified any more. className={}", className);
                }
            }
        } catch (Exception e) {
            LOG.error("Error transforming class", e);
        }
        return transformed;
    }
}
