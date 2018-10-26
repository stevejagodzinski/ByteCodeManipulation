package com.github.stevejagodzinski.bcm.transformers.httpservlet;

public interface HttpRequestWrapper {
    void insertBefore(StringBuilder code);

    void insertAfter(StringBuilder code);
}
