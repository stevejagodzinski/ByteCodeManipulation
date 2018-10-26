package com.github.stevejagodzinski.bcm.transformers.httpservlet;

import com.github.stevejagodzinski.bcm.clazz.ClassNameConverter;
import com.github.stevejagodzinski.bcm.transformers.AbstractClassFileTransformer;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    protected void transformCtClass(CtClass ctClass) throws NoSuchMethodException, CannotCompileException {
        CtMethod serviceMethod = findServiceMethod(ctClass);
        transformServiceMethod(serviceMethod);
    }

    private CtMethod findServiceMethod(CtClass httpServletClass) throws NoSuchMethodException {
        for(CtMethod method : httpServletClass.getDeclaredMethods()) {
            if (AccessFlag.isProtected(method.getMethodInfo().getAccessFlags()) && method.getName().equals("service")) {
                return method;
            }
        }

        throw new NoSuchMethodException("Can not find a method named 'service' with 'protected' access in class " + httpServletClass);
    }

    private void transformServiceMethod(CtMethod serviceMethod) throws CannotCompileException {
        insertCodeBeforeServiceMethod(serviceMethod);
        insertCodeAfterServiceMethod(serviceMethod);
    }

    private void insertCodeBeforeServiceMethod(CtMethod serviceMethod) throws CannotCompileException {
        StringBuilder code = new StringBuilder();
        addLocalRequestIdVariable(code);
        this.httpRequestWrappers.forEach(w -> w.insertBefore(code));
        serviceMethod.insertBefore(code.toString());
    }

    private void insertCodeAfterServiceMethod(CtMethod serviceMethod) throws CannotCompileException {
        StringBuilder code = new StringBuilder();
        this.httpRequestWrappers.forEach(w -> w.insertAfter(code));
        serviceMethod.insertAfter(code.toString());
    }

    private void addLocalRequestIdVariable(StringBuilder code) {
        code.append("java.util.UUID requestId = java.util.UUID.randomUUID();");
        code.append("$2.addHeader(\"X-SJ-UUID\", requestId.toString());");
    }
}
