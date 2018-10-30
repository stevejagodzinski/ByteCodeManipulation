package com.github.stevejagodzinski.bcm.time;

import java.util.Optional;
import java.util.UUID;

public class RequestTimer {
   private final NanoTimeProvider nanoTimeProvider;
   private final UUID requestId;
   private final long startTime;

   private Long endTime;

   public RequestTimer(UUID requestId) {
      this(requestId, SystemNanoTimeProvider.INSTANCE);
   }

   public RequestTimer(UUID requestId, NanoTimeProvider nanoTimeProvider) {
      this.requestId = requestId;
      this.nanoTimeProvider = nanoTimeProvider;
      this.startTime = nanoTimeProvider.nanoTime();

   }

   public void requestComplete() {
      if (this.endTime == null) {
         this.endTime = nanoTimeProvider.nanoTime();
      } else {
         throw new IllegalStateException("Request is already complete");
      }
   }

   public Long getTotalTimeNanos() {
      return Optional.ofNullable(this.endTime).map(endTime -> endTime - this.startTime).orElse(null);
   }

   @Override public String toString() {
      return "RequestTimer{" +
            "requestId=" + requestId +
            ", totalTimeNanos=" + getTotalTimeNanos() +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
   }
}
