package com.github.stevejagodzinski.bcm.transformers;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;
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
                    CtMethod serviceMethod = findServiceMethod(ctClass);
                    addUniqueIdToResponse(serviceMethod);
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

    private CtMethod findServiceMethod(CtClass httpServletClass) throws NoSuchMethodException {
        for(CtMethod method : httpServletClass.getDeclaredMethods()) {
            if (AccessFlag.isProtected(method.getMethodInfo().getAccessFlags()) && method.getName().equals("service")) {
                return method;
            }
        }

        throw new NoSuchMethodException("Can not find a method named 'service' with 'protected' access in class HttpServlet");
    }

    private void addUniqueIdToResponse(CtMethod serviceMethod) throws CannotCompileException {
        serviceMethod.insertAfter(generateAddUniqueIdToResponseJavaCode());
    }

    private String generateAddUniqueIdToResponseJavaCode() {
        // TODO: Make key configurable
        return "$2.addHeader(\"X-SJ-UUID\", UUID.randomUUID().toString());";
    }
}
