package com.github.stevejagodzinski.bcm.transformers.httpservlet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.stevejagodzinski.bcm.clazz.ClassNameConverter;
import com.github.stevejagodzinski.bcm.transformers.AbstractClassFileTransformer;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;

public class HttpRequestTransformer extends AbstractClassFileTransformer {

    private static final String TARGET_CLASS_SLASH = "javax/servlet/http/HttpServlet";
    public static final String TARGET_CLASS_DOT = ClassNameConverter.toDotSeparated(TARGET_CLASS_SLASH);

    private final List<HttpRequestWrapper> httpRequestWrappers = new CopyOnWriteArrayList<>();

    public HttpRequestTransformer() {
        super(TARGET_CLASS_SLASH);
    }

    public void addHttpRequestWrapper(HttpRequestWrapper httpRequestWrapper) {
        this.httpRequestWrappers.add(httpRequestWrapper);
    }

    @Override
    protected void transformCtClass(CtClass ctClass, ClassPool classPool) throws NoSuchMethodException, CannotCompileException, NotFoundException {
        CtMethod serviceMethod = findServiceMethod(ctClass);
        transformServiceMethod(serviceMethod, classPool);
    }

    private CtMethod findServiceMethod(CtClass httpServletClass) throws NoSuchMethodException {
        for(CtMethod method : httpServletClass.getDeclaredMethods()) {
            if (AccessFlag.isProtected(method.getMethodInfo().getAccessFlags()) && method.getName().equals("service")) {
                return method;
            }
        }

        throw new NoSuchMethodException("Can not find a method named 'service' with 'protected' access in class " + httpServletClass);
    }

    private void transformServiceMethod(CtMethod serviceMethod, ClassPool classPool) throws CannotCompileException, NotFoundException {
        addLocalVariables(serviceMethod, classPool);
        insertCodeBeforeServiceMethod(serviceMethod);
        insertCodeAfterServiceMethod(serviceMethod);
    }

    private void addLocalVariables(CtMethod serviceMethod, ClassPool classPool) throws CannotCompileException, NotFoundException {
        addLocalRequestIdVariable(serviceMethod, classPool);

        for (HttpRequestWrapper wrapper : this.httpRequestWrappers) {
            wrapper.addLocalVariables(serviceMethod, classPool);
        }
    }

    private void insertCodeBeforeServiceMethod(CtMethod serviceMethod) throws CannotCompileException {
        StringBuilder code = new StringBuilder();
        initializeLocalRequestIdVariable(code);
        this.httpRequestWrappers.forEach(w -> w.insertBefore(code));
        serviceMethod.insertBefore(code.toString());
    }

    private void insertCodeAfterServiceMethod(CtMethod serviceMethod) throws CannotCompileException {
        StringBuilder code = new StringBuilder();
        this.httpRequestWrappers.forEach(w -> w.insertAfter(code));
        serviceMethod.insertAfter(code.toString());
    }

    private void addLocalRequestIdVariable(CtMethod serviceMethod, ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtClass ctUuidClass = classPool.get("java.util.UUID");
        serviceMethod.addLocalVariable("requestId", ctUuidClass);
    }

    private void initializeLocalRequestIdVariable(StringBuilder code) {
        code.append("requestId = java.util.UUID.randomUUID();");
    }
}
