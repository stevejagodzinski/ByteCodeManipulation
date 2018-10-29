package com.github.stevejagodzinski.bcm.transformers.httpservlet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;

public interface HttpRequestWrapper {
    void addLocalVariables(CtMethod serviceMethod, ClassPool classPool) throws NotFoundException, CannotCompileException;

    void insertBefore(StringBuilder code);

    void insertAfter(StringBuilder code);
}
