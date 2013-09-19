package eu32k.spaceDingus.core.system.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;

public class HealthRenderSystem extends EntityProcessingSystem {

   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<HealthComponent> hm;
   private ComponentMapper<ShieldComponent> sm;

   private Camera camera;
   private ShapeRenderer shapeRenderer;

   @SuppressWarnings("unchecked")
   public HealthRenderSystem(Camera camera) {
      super(Aspect.getAspectForAll(ActorComponent.class).one(HealthComponent.class, ShieldComponent.class));
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      am = world.getMapper(ActorComponent.class);
      hm = world.getMapper(HealthComponent.class);
      sm = world.getMapper(ShieldComponent.class);

      shapeRenderer = new ShapeRenderer();
   }

   @Override
   protected void begin() {
      shapeRenderer.setProjectionMatrix(camera.combined);
      shapeRenderer.begin(ShapeType.Filled);
   }

   @Override
   public void process(Entity e) {
      Actor actor = am.get(e).actor;

      float width = 1.0f;
      float height = 0.05f;

      if (hm.has(e)) {
         HealthComponent health = hm.get(e);
         shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.5f);
         shapeRenderer.rect(actor.getX() - width / 2.0f, actor.getY() + 0.6f, width, height);

         if (health.health > 0.0f) {
            shapeRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            shapeRenderer.rect(actor.getX() - width / 2.0f, actor.getY() + 0.6f, width * (health.health / health.maxHealth), height);
         }
      }

      if (sm.has(e)) {
         ShieldComponent shield = sm.get(e);
         shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1.0f);
         shapeRenderer.rect(actor.getX() - width / 2.0f, actor.getY() + 0.5f, width, height);

         if (shield.shield > 0.0f) {
            shapeRenderer.setColor(0.0f, 0.3f, 1.0f, 1.0f);
            shapeRenderer.rect(actor.getX() - width / 2.0f, actor.getY() + 0.5f, width * (shield.shield / shield.maxShield), height);
         }
      }
   }

   @Override
   protected void end() {
      shapeRenderer.end();
   }

}