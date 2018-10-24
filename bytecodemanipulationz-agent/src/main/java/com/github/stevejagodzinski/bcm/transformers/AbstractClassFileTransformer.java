package com.github.stevejagodzinski.bcm.transformers;

import com.github.stevejagodzinski.bcm.clazz.ClassNameConverter;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

abstract class AbstractClassFileTransformer implements ClassFileTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractClassFileTransformer.class);

    private static final ClassPool pool = ClassPool.getDefault();

    private final String targetClass;

    protected AbstractClassFileTransformer(String targetClass) {
        this.targetClass = targetClass;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] transformed = null;
        try {
            if (targetClass.equals(className)) {
                final ClassLoader contextClassLoader = getClassLoader();
                pool.insertClassPath(new LoaderClassPath(contextClassLoader));

                String dotSeparatedClassName = ClassNameConverter.toDotSeparated(className);
                CtClass ctClass = pool.get(dotSeparatedClassName);

                if (!ctClass.isFrozen()) {
                    LOG.info("Instrumenting the {} class", className);
                    transformCtClass(ctClass);
                    transformed = ctClass.toBytecode();
                } else {
                    LOG.error("Class has already been loaded or written out and thus it cannot be modified any more. className={}", className);
                }
            } else {
                LOG.trace("Class {} will not be instrumented", className);
            }
        } catch (NotFoundException | IOException | CannotCompileException | NoSuchMethodException | RuntimeException e) {
            LOG.trace("Error transforming class {}", className, e);
        }
        return transformed;
    }

    protected abstract void transformCtClass(CtClass ctClass) throws NoSuchMethodException, CannotCompileException;

    private ClassLoader getClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if (contextClassLoader == null) {
            contextClassLoader = ClassLoader.getSystemClassLoader();
        }

        return contextClassLoader;
    }
}
