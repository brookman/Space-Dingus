package eu32k.spaceDingus.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;

import eu32k.spaceDingus.core.common.Textures;

public class SpaceDingusScene implements ApplicationListener {

   private Stage stage;

   public SpaceDingusScene() {

   }

   private class MyActor extends Actor {
      private TextureRegion region;

      public MyActor() {
         region = new TextureRegion(Textures.get("textures/bullet.png"));
      }

      @Override
      public void draw(SpriteBatch batch, float parentAlpha) {
         Color color = getColor();
         batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
         batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
      }

   }

   @Override
   public void create() {
      stage = new Stage(8.0f, 4.8f, true);

      Group group = new Group();

      MyActor actor = new MyActor();
      actor.setSize(1.0f, 1.0f);
      actor.setScaleX(1.0f);
      actor.setRotation(0);
      group.addActor(actor);

      MyActor actor2 = new MyActor();
      actor2.setSize(0.2f, 0.2f);
      actor2.setScaleX(1.0f);
      actor2.setRotation(0);
      group.addActor(actor2);

      group.setScaleX(2.0f);
      group.setRotation(10);

      stage.addActor(group);
   }

   @Override
   public void render() {
      Gdx.gl.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
   }

   @Override
   public void pause() {
      // TODO Auto-generated method stub

   }

   @Override
   public void resume() {
      // TODO Auto-generated method stub

   }

   @Override
   public void resize(int width, int height) {
      Vector2 size = Scaling.fit.apply(8.0f, 4.8f, width, height);
      int viewportX = (int) (width - size.x) / 2;
      int viewportY = (int) (height - size.y) / 2;
      int viewportWidth = (int) size.x;
      int viewportHeight = (int) size.y;
      Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
      stage.setViewport(stage.getWidth(), stage.getHeight(), true, viewportX, viewportY, viewportWidth, viewportHeight);
   }

   @Override
   public void dispose() {
      stage.dispose();
   }
}
