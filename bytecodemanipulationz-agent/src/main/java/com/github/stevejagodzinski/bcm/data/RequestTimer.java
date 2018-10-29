package com.github.stevejagodzinski.bcm.data;

import java.util.UUID;

public class RequestTimer {
   private final UUID requestId;
   private final Long startTime = System.nanoTime();
   private Long endTime;

   public RequestTimer(UUID requestId) {
      this.requestId = requestId;
   }

   public void requestComplete() {
      this.endTime = System.nanoTime();
   }

   @Override public String toString() {
      return "RequestTimer{" +
            "requestId=" + requestId +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
   }
}
