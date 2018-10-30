package com.github.stevejagodzinski.bcm.transformers.httpservlet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class TimeRequestFromStartToFinish extends DefaultHttpRequestWrapper {
   public static final TimeRequestFromStartToFinish INSTANCE = new TimeRequestFromStartToFinish();

   private TimeRequestFromStartToFinish() {}

   @Override
   public void addLocalVariables(CtMethod serviceMethod, ClassPool classPool) throws NotFoundException, CannotCompileException {
      CtClass requestTimeCtClass = classPool.getCtClass("com.github.stevejagodzinski.bcm.time.RequestTimer");
      serviceMethod.addLocalVariable("requestTimes", requestTimeCtClass);
   }

   @Override
   public void insertBefore(StringBuilder code) {
      code.append("requestTimes = new com.github.stevejagodzinski.bcm.time.RequestTimer(requestId);");
   }

   @Override
   public void insertAfter(StringBuilder code) {
      code.append("requestTimes.requestComplete();");
   }
}
