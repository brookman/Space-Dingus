package eu32k.gdx.common;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class DebugRenderer {

   private static Camera camera;
   private static ShapeRenderer shapeRenderer;

   public static void init(Camera camera) {
      DebugRenderer.camera = camera;
      shapeRenderer = new ShapeRenderer();
   }

   public static void begin(ShapeType type) {
      shapeRenderer.setProjectionMatrix(camera.combined);
      shapeRenderer.begin(type);
   }

   public static ShapeRenderer getRenderer() {
      return shapeRenderer;
   }

   public static void end() {
      shapeRenderer.end();
   }
}
