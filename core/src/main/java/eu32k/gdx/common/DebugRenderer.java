package eu32k.gdx.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class DebugRenderer {

   private static Camera camera;
   private static ShapeRenderer shapeRenderer;
   private static SpriteBatch batch;
   private static BitmapFont debugFont;

   public static void init(Camera camera) {
      DebugRenderer.camera = camera;
      shapeRenderer = new ShapeRenderer();
      batch = new SpriteBatch();
      debugFont = new BitmapFont(Gdx.files.internal("fonts/xolonium_green.fnt"), new TextureRegion(Textures.get("fonts/xolonium_green.png")), false);
      debugFont.setScale(0.0017f);
      debugFont.setUseIntegerPositions(false);
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

   public static void drawText(String text, float x, float y, boolean unproject) {
      Vector3 pos = new Vector3(x, y, 0);
      if (unproject) {
         camera.unproject(pos);
      }
      batch.setProjectionMatrix(camera.combined);
      batch.begin();
      debugFont.drawMultiLine(batch, text, pos.x - debugFont.getMultiLineBounds(text).width / 2, pos.y);
      batch.end();
   }
}
