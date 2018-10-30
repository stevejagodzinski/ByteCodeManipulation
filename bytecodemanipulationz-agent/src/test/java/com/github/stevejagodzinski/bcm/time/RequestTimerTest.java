package com.github.stevejagodzinski.bcm.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.UUID;

import org.junit.Test;

public class RequestTimerTest {
   private static final class TestNanoTimeProvider implements NanoTimeProvider {
      private int i = 10;

      @Override public long nanoTime() {
         i = i * 100;
         return i;
      }
   }

   @Test
   public void testTotalTimeBeforeRequestCompleted() {
      // Given
      UUID requestId = UUID.randomUUID();
      RequestTimer requestTimer = new RequestTimer(requestId, new TestNanoTimeProvider());

      // When - Request not completed

      // Then
      Long totalTime = requestTimer.getTotalTimeNanos();
      assertThat("totalTime is null", totalTime, is(nullValue()));
   }

   @Test
   public void testTotalTimeAfterRequestCompleted() {
      // Given
      UUID requestId = UUID.randomUUID();
      RequestTimer requestTimer = new RequestTimer(requestId, new TestNanoTimeProvider());

      // When
      requestTimer.requestComplete();

      // Then
      Long totalTime = requestTimer.getTotalTimeNanos();
      assertThat(totalTime, equalTo(100000L - 1000L));
   }

   @Test(expected = IllegalStateException.class)
   public void testInvocationOfRequestCompleteTwiceThrowsIllegalStateException() {
      // Given
      UUID requestId = UUID.randomUUID();
      RequestTimer requestTimer = new RequestTimer(requestId, new TestNanoTimeProvider());
      requestTimer.requestComplete();

      // When
      requestTimer.requestComplete();
   }

   @Test
   public void testToString() {
      // Given
      UUID requestId = UUID.randomUUID();
      RequestTimer requestTimer = new RequestTimer(requestId, new TestNanoTimeProvider());
      requestTimer.requestComplete();

      // When
      String actual = requestTimer.toString();

      // Then
      String expected = "RequestTimer{requestId=" + requestId + ", totalTimeNanos=99000, startTime=1000, endTime=100000}";
      assertThat(actual, equalTo(expected));
   }


}
