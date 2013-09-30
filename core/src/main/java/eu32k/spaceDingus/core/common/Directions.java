package eu32k.spaceDingus.core.common;

public class Directions {
   public static final int TRANSLATE_FORWARD = 1;
   public static final int TRANSLATE_BACKWARD = 2;
   public static final int TRANSLATE_LEFT = 4;
   public static final int TRANSLATE_RIGHT = 8;
   public static final int ROTATE_LEFT = 16;
   public static final int ROTATE_RIGHT = 32;

   public static int getDirections(boolean translateForward, boolean translateBackward, boolean translateLeft, boolean translateRight, boolean rotateLeft, boolean rotateRight) {
      return (translateForward ? Directions.TRANSLATE_FORWARD : 0) //
            | (translateBackward ? Directions.TRANSLATE_BACKWARD : 0) //
            | (translateLeft ? Directions.TRANSLATE_LEFT : 0) //
            | (translateRight ? Directions.TRANSLATE_RIGHT : 0) //
            | (rotateLeft ? Directions.ROTATE_LEFT : 0) //
            | (rotateRight ? Directions.ROTATE_RIGHT : 0);
   }

   public static int get(int... directions) {
      int result = 0;
      for (int direction : directions) {
         result |= direction;
      }
      return result;
   }

   public static boolean compareOr(int d1, int d2) {
      return (d1 & d2) != 0;
   }
}
