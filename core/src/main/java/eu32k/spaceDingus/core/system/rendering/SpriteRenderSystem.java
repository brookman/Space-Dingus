package eu32k.spaceDingus.core.system.rendering;

import java.util.Comparator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.Sort;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.spaceDingus.core.common.Textures;
import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class SpriteRenderSystem extends EntitySystem {

   private ComponentMapper<SpriteComponent> sm;
   private ComponentMapper<EngineComponent> em;
   private ComponentMapper<ActorComponent> am;

   private Camera camera;
   private SpriteBatch batch;

   private Texture background1;
   private Texture background2;

   @SuppressWarnings("unchecked")
   public SpriteRenderSystem(Camera camera) {
      super(Aspect.getAspectForAll(SpriteComponent.class, ActorComponent.class));
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      sm = world.getMapper(SpriteComponent.class);
      em = world.getMapper(EngineComponent.class);
      am = world.getMapper(ActorComponent.class);
      batch = new SpriteBatch();
      background1 = Textures.get("textures/stars1.png");
      background2 = Textures.get("textures/stars2.png");
   }

   @Override
   protected void begin() {
      batch.setProjectionMatrix(camera.combined);
      batch.begin();
   }

   private void drawBackGround() {
      float pax = camera.position.x / 3;
      float pay = camera.position.y / 3;

      batch.draw(background2, camera.position.x - 5, camera.position.y - 5, 10, 10, pax, pay, pax + 3, pay + 3);

      pax = camera.position.x / 5;
      pay = camera.position.y / 5;

      batch.draw(background2, camera.position.x - 5, camera.position.y - 5, 10, 10, pax, pay, pax + 4, pay + 4);

      pax = camera.position.x / 10;
      pay = camera.position.y / 10;

      batch.draw(background1, camera.position.x - 5, camera.position.y - 5, 10, 10, pax, pay, pax + 7, pay + 7);
   }

   @Override
   protected void processEntities(ImmutableBag<Entity> entities) {
      drawBackGround();
      Bag<Entity> bag = (Bag<Entity>) entities;
      Sort.instance().sort(bag, new Comparator<Entity>() {
         @Override
         public int compare(Entity e1, Entity e2) {
            return sm.get(e1).layer - sm.get(e2).layer;
         }
      });
      for (Entity e : bag) {
         process(e);
      }
   }

   public void process(Entity e) {
      Sprite sprite = sm.get(e).sprite;
      Actor actor = am.get(e).actor;

      if (em.has(e) && em.get(e).isRunning) {
         batch.setColor(1.0f, 0.0f, 0.0f, sm.get(e).alpha);
      } else {
         batch.setColor(1.0f, 1.0f, 1.0f, sm.get(e).alpha);
      }

      batch.draw(sprite, actor.getX() - sprite.getWidth() / 2, actor.getY() - sprite.getHeight() / 2, sprite.getWidth() / 2, sprite.getHeight() / 2, sprite.getWidth(), sprite.getHeight(), 1f, 1f,
            actor.getRotation());
   }

   @Override
   protected void end() {
      batch.end();
   }

   @Override
   protected boolean checkProcessing() {
      return true;
   }
}
