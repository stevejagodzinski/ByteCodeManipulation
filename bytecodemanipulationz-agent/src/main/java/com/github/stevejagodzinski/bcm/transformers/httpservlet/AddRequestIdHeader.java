package com.github.stevejagodzinski.bcm.transformers.httpservlet;

public class AddRequestIdHeader extends DefaultHttpRequestWrapper {

    public static final AddRequestIdHeader INSTANCE = new AddRequestIdHeader();

    private AddRequestIdHeader() { }

    @Override
    public void insertBefore(StringBuilder code) {
        code.append("$2.addHeader(\"X-SJ-UUID\", requestId.toString());");
    }
}
