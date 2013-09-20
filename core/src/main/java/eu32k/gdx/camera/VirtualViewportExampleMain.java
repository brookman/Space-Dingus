package eu32k.gdx.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class VirtualViewportExampleMain extends com.badlogic.gdx.Game {

   private OrthographicCameraWithVirtualViewport camera;

   // extra stuff for the example
   private SpriteBatch spriteBatch;
   private Sprite minimumAreaSprite;
   private Sprite maximumAreaSprite;
   private Sprite floatingButtonSprite;
   private BitmapFont font;

   private MultipleVirtualViewportBuilder multipleVirtualViewportBuilder;

   @Override
   public void create() {
      multipleVirtualViewportBuilder = new MultipleVirtualViewportBuilder(800, 480, 854, 600);
      VirtualViewport virtualViewport = multipleVirtualViewportBuilder.getVirtualViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

      camera = new OrthographicCameraWithVirtualViewport(virtualViewport);
      // centers the camera at 0, 0 (the center of the virtual viewport)
      camera.position.set(0f, 0f, 0f);

      // extra code
      spriteBatch = new SpriteBatch();

      Pixmap pixmap = new Pixmap(64, 64, Format.RGBA8888);
      pixmap.setColor(Color.WHITE);
      pixmap.fillRectangle(0, 0, 64, 64);

      minimumAreaSprite = new Sprite(new Texture(pixmap));
      minimumAreaSprite.setPosition(-400, -240);
      minimumAreaSprite.setSize(800, 480);
      minimumAreaSprite.setColor(0f, 1f, 0f, 1f);

      maximumAreaSprite = new Sprite(new Texture(pixmap));
      maximumAreaSprite.setPosition(-427, -300);
      maximumAreaSprite.setSize(854, 600);
      maximumAreaSprite.setColor(1f, 1f, 0f, 1f);

      floatingButtonSprite = new Sprite(new Texture(pixmap));
      floatingButtonSprite.setPosition(virtualViewport.getVirtualWidth() * 0.5f - 80, virtualViewport.getVirtualHeight() * 0.5f - 80);
      floatingButtonSprite.setSize(64, 64);
      floatingButtonSprite.setColor(1f, 1f, 1f, 1f);

      font = new BitmapFont();
      font.setColor(Color.BLACK);
   }

   @Override
   public void resize(int width, int height) {
      super.resize(width, height);

      VirtualViewport virtualViewport = multipleVirtualViewportBuilder.getVirtualViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      camera.setVirtualViewport(virtualViewport);

      camera.updateViewport();
      // centers the camera at 0, 0 (the center of the virtual viewport)
      camera.position.set(0f, 0f, 0f);

      // relocate floating stuff
      // Button button = actorsFactory.createSoundsButton(localResourceManager, 1f, audioPlayer.isSoundMuted(), -427f + virtualViewport.getVirtualWidth() * 0.5f, virtualViewport.getVirtualHeight() *
      // 0.5f, 1.2f, 1.2f, clickListener);
      floatingButtonSprite.setPosition(virtualViewport.getVirtualWidth() * 0.5f - 80, virtualViewport.getVirtualHeight() * 0.5f - 80);
   }

   @Override
   public void render() {
      super.render();
      Gdx.gl.glClearColor(1f, 0f, 0f, 1f);
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
      camera.update();

      // render stuff...
      spriteBatch.setProjectionMatrix(camera.combined);
      spriteBatch.begin();
      maximumAreaSprite.draw(spriteBatch);
      minimumAreaSprite.draw(spriteBatch);
      floatingButtonSprite.draw(spriteBatch);
      font.draw(spriteBatch, String.format("%1$sx%2$s", Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), -20, 0);
      spriteBatch.end();
   }

}
