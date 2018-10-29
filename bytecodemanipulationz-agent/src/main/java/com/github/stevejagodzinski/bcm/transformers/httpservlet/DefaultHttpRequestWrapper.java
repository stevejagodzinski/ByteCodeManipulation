package com.github.stevejagodzinski.bcm.transformers.httpservlet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;

abstract class DefaultHttpRequestWrapper implements HttpRequestWrapper {
    @Override
    public void addLocalVariables(CtMethod serviceMethod, ClassPool classPool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void insertBefore(StringBuilder code) {

    }

    @Override
    public void insertAfter(StringBuilder code) {

    }
}
