package com.github.stevejagodzinski.bcm.time;

public class SystemNanoTimeProvider implements NanoTimeProvider {
   public static final SystemNanoTimeProvider INSTANCE = new SystemNanoTimeProvider();

   private SystemNanoTimeProvider() {}

   @Override
   public long nanoTime() {
      return System.nanoTime();
   }
}
