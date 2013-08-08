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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class SpriteRenderSystem extends EntitySystem {

   private ComponentMapper<SpriteComponent> sm;
   private ComponentMapper<EngineComponent> em;
   private ComponentMapper<TransformComponent> tm;

   private Camera camera;
   private SpriteBatch batch;

   @SuppressWarnings("unchecked")
   public SpriteRenderSystem(Camera camera) {
      super(Aspect.getAspectForAll(SpriteComponent.class, TransformComponent.class));
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      sm = world.getMapper(SpriteComponent.class);
      em = world.getMapper(EngineComponent.class);
      tm = world.getMapper(TransformComponent.class);
      batch = new SpriteBatch();
   }

   @Override
   protected void begin() {
      batch.setProjectionMatrix(camera.combined);
      batch.begin();
   }

   @Override
   protected void processEntities(ImmutableBag<Entity> entities) {
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
      TransformComponent pos = tm.get(e);

      if (em.has(e) && em.get(e).isRunning) {
         batch.setColor(1.0f, 0.0f, 0.0f, sm.get(e).alpha);
      } else {
         batch.setColor(1.0f, 1.0f, 1.0f, sm.get(e).alpha);
      }

      batch.draw(sprite, pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2, sprite.getWidth() / 2, sprite.getHeight() / 2, sprite.getWidth(), sprite.getHeight(), 1f, 1f, pos.rotation);
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
