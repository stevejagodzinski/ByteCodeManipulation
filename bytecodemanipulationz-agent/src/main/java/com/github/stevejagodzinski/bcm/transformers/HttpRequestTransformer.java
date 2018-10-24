package com.github.stevejagodzinski.bcm.transformers;

import com.github.stevejagodzinski.bcm.clazz.ClassNameConverter;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;

public class HttpRequestTransformer extends AbstractClassFileTransformer {

    private static final String TARGET_CLASS_SLASH = "javax/servlet/http/HttpServlet";
    public static final String TARGET_CLASS_DOT = ClassNameConverter.toDotSeparated(TARGET_CLASS_SLASH);

    public HttpRequestTransformer() {
        super(TARGET_CLASS_SLASH);
    }

    @Override
    protected void transformCtClass(CtClass ctClass) throws NoSuchMethodException, CannotCompileException {
        CtMethod serviceMethod = findServiceMethod(ctClass);
        addUniqueIdToResponse(serviceMethod);
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
        return "$2.addHeader(\"X-SJ-UUID\", java.util.UUID.randomUUID().toString());";
    }
}
