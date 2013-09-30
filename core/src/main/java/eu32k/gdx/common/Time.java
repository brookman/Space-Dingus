package eu32k.gdx.common;

public class Time {

   private static boolean paused = false;
   private static long pausedTime;
   private static long delayTime;

   public static long getTime() {
      if (paused) {
         return pausedTime;
      }
      return System.currentTimeMillis() - delayTime;
   }

   public static void setPaused(boolean paused) {
      Time.paused = paused;
      if (paused) {
         pausedTime = System.currentTimeMillis();
      } else {
         delayTime += System.currentTimeMillis() - pausedTime;
      }
   }
}
