package com.github.stevejagodzinski.bcm.transformers;

import com.github.stevejagodzinski.bcm.agent.attach.AgentInstaller;
import com.github.stevejagodzinski.bcm.servlet.NoOpHttpServlet;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpRequestTransformerTest {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestTransformerTest.class);

    @Before
    public void before() throws AgentInitializationException, AgentLoadException, AttachNotSupportedException, IOException {
        installAgent();
    }

    private void installAgent() throws AgentInitializationException, AgentLoadException, AttachNotSupportedException, IOException {
        AgentInstaller.INSTANCE.installAgent();
    }

    @Test
    public void testInstrumentedServiceMethodAddsUniqueIdToResponse() throws ServletException, IOException {
        // Given I have some HTTP request
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("INVALID");

        // When I service the request
        HttpServlet servlet = createNoOpServlet();

        HttpServletResponse response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        // Then, the response have a valid UUID
        ArgumentCaptor<String> uuidCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(eq("X-SJ-UUID"), uuidCaptor.capture());

        String uuidString = uuidCaptor.getValue();
        assertNotNull("X-SJ-UUID header was not added to the HTTP response", uuidString);

        UUID uuid = UUID.fromString(uuidString);
        assertNotNull("X-SJ-UUID header added to the HTTP response is not a valid UUID", uuid);
    }

    private HttpServlet createNoOpServlet() {
        return new NoOpHttpServlet();
    }
}
