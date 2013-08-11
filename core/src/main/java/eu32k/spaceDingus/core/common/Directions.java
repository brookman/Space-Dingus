package eu32k.spaceDingus.core.common;

public class Directions {
   public static final int translateForward = 1;
   public static final int translateBackward = 2;
   public static final int translateLeft = 4;
   public static final int translateRight = 8;
   public static final int rotateLeft = 16;
   public static final int rotateRight = 32;

   public static int getDirections(boolean translateForward, boolean translateBackward, boolean translateLeft, boolean translateRight, boolean rotateLeft, boolean rotateRight) {
      return (translateForward ? Directions.translateForward : 0) //
            | (translateBackward ? Directions.translateBackward : 0) //
            | (translateLeft ? Directions.translateLeft : 0) //
            | (translateRight ? Directions.translateRight : 0) //
            | (rotateLeft ? Directions.rotateLeft : 0) //
            | (rotateRight ? Directions.rotateRight : 0);
   }

   public static boolean compareOr(int d1, int d2) {
      return (d1 & d2) != 0;
   }
}
