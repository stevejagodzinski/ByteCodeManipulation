package com.github.stevejagodzinski.bcm.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

public class NoOpHttpServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(NoOpHttpServlet.class);

    static {
        LOG.debug("Class loaded");
    }
}
