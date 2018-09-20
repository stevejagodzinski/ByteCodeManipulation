package com.github.stevejagodzinski.bcm.transformers;

import com.github.stevejagodzinski.bcm.classloader.ClassLoaderService;
import com.github.stevejagodzinski.bcm.clazz.ClassNameConverter;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.scopedpool.ScopedClassPoolRepository;
import javassist.scopedpool.ScopedClassPoolRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;

public class HttpRequestTransformer implements ClassFileTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestTransformer.class);

    public static final String TARGET_CLASS_SLASH = "javax/servlet/http/HttpServlet";
    public static final String TARGET_CLASS_DOT = "javax.servlet.http.HttpServlet";

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] transformed = null;
        try {

            ClassPool pool = getClassPool(getClassLoader(className));

            insertClassPathToClassPool(pool, className);
            CtClass ctClass = pool.get(className);

            if (shouldInstrument(ctClass)) {

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

    private boolean shouldInstrument(CtClass ctClass) {
        return isGenericOrSpringHttpServlet(ctClass.getName());
    }

    private boolean isHttpServlet(CtClass ctClass) throws NotFoundException {
        return ctClass != null && !isJavaLangObject(ctClass) && (TARGET_CLASS_SLASH.equals(ctClass.getName()) || isHttpServlet(ctClass.getSuperclass()));
    }

    private boolean isGenericOrSpringHttpServlet(String className) {
        return TARGET_CLASS_SLASH.equals(className) || "org/springframework/web/servlet/FrameworkServlet".equals(className);
    }

    private boolean isJavaLangObject(CtClass ctClass) {
        return "java/lang/Object".equals(ctClass.getName());
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
        serviceMethod.insertBefore(generateAddUniqueIdToResponseJavaCode());
    }

    private String generateAddUniqueIdToResponseJavaCode() {
        // TODO: Make key configurable
        return "$2.addHeader(\"X-SJ-UUID\", java.util.UUID.randomUUID().toString());";
    }

    private void insertClassPathToClassPool(ClassPool pool, String className) throws ClassNotFoundException {
        ClassLoader classLoader = getClassLoader(className);

//        insertUsingClassClassPath(pool, className);
        insertUsingClassLoader(pool, classLoader, className);
    }

    private void insertUsingClassClassPath(ClassPool pool, String className) throws ClassNotFoundException {
        String dotSeparatedClassName = ClassNameConverter.toDotSeparated(className);
        Class clazz = Class.forName(dotSeparatedClassName);
        pool.insertClassPath(new ClassClassPath(clazz));
    }

    private void insertUsingClassLoader(ClassPool pool, ClassLoader classLoader, String className) {
        LOG.debug("Inserting Class Path into ClassPool. className=[{}] ClassLoader[{}]", className, classLoader);
        pool.insertClassPath(new LoaderClassPath(classLoader));
    }

    private ClassLoader getClassLoader(String className) {
        return ClassLoaderService.getCurrentContextClassLoader();
    }

    private void insertUsingUrlClassLoader(ClassPool pool, ClassLoader sysClassLoader) {
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        for (int i = 0; i < urls.length; i++) {
            try {
                // insert all classpaths into the javassist classpool
                pool.insertClassPath(urls[i].getFile());
            } catch (NotFoundException e) {
                LOG.error("Error adding ClassLoader to ClassPool", e);
            }
        }
    }

    private ClassPool getClassPool(ClassLoader classLoader) {
        ScopedClassPoolRepository scopedClassPoolRepository = ScopedClassPoolRepositoryImpl.getInstance();
        return scopedClassPoolRepository.registerClassLoader(classLoader);
    }
}
