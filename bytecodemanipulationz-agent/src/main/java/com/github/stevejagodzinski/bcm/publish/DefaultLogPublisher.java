package com.github.stevejagodzinski.bcm.publish;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogPublisher implements LogPublisher {

   private static final Logger LOG = LoggerFactory.getLogger(DefaultLogPublisher.class);

   private static DefaultLogPublisher INSTANCE = new DefaultLogPublisher();

   private DefaultLogPublisher() {}

   @Override
   public void writeLog(Loggable loggable) {
      LOG.info(Objects.toString(loggable));
   }
}
