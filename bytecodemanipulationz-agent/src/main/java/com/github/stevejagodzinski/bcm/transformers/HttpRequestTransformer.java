package com.github.stevejagodzinski.bcm.transformers;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class HttpRequestTransformer implements ClassFileTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestTransformer.class);

    public static final String TARGET_CLASS_SLASH = "javax/servlet/http/HttpServlet";
    public static final String TARGET_CLASS_DOT = "javax.servlet.http.HttpServlet";

    private final ClassPool pool = ClassPool.getDefault();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] transformed = null;
        try {
            //pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));



            if (TARGET_CLASS_SLASH.equals(className)) {

                final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader != null)
                {
                    pool.insertClassPath(new LoaderClassPath(contextClassLoader));
                }

                CtClass ctClass = pool.get(TARGET_CLASS_DOT);
                LOG.info("Instrumenting the {} class", className);
                if (!ctClass.isFrozen()) {
                    // TODO: Add specific transformations to the ctClass
                    CtMethod serviceMethod = findServiceMethod(ctClass);
                    addUniqueIdToResponse(serviceMethod);
                    transformed = ctClass.toBytecode();
                } else {
                    LOG.error("Class has already been loaded or written out and thus it cannot be modified any more. className={}", className);
                }
            } else {
                LOG.trace("Class {} will not be instrumented", className);
            }
        } catch (NotFoundException e) {
            LOG.trace("Error transforming class", e);
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

        throw new NoSuchMethodException("Can not find a method named 'service' with 'protected' access in class " + httpServletClass);
    }

    private void addUniqueIdToResponse(CtMethod serviceMethod) throws CannotCompileException {
        serviceMethod.insertAfter(generateAddUniqueIdToResponseJavaCode());
    }

    private String generateAddUniqueIdToResponseJavaCode() {
        // TODO: Make key configurable
        return "$2.addHeader(\"X-SJ-UUID\", java.util.UUID.randomUUID().toString());";
    }
}
