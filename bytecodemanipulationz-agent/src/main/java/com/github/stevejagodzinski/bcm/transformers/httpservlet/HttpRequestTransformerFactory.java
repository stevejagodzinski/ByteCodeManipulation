package com.github.stevejagodzinski.bcm.transformers.httpservlet;

public class HttpRequestTransformerFactory {

    private HttpRequestTransformerFactory() {
        throw new UnsupportedOperationException("Class with only static factory methods should not be instantiated");
    }

    public static HttpRequestTransformer newInstance() {
        HttpRequestTransformer transformer = new HttpRequestTransformer();
        transformer.addHttpRequestWrapper(AddRequestIdHeader.INSTANCE);
        return transformer;
    }
}
