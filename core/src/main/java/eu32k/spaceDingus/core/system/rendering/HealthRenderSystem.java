package eu32k.spaceDingus.core.system.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

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
      super(Aspect.getAspectForAll(ActorComponent.class, HealthComponent.class));
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

      Vector2 actorPos = am.get(e).actor.getPositionOnStage();

      float width = 0.9f;
      float height = 0.05f;

      HealthComponent health = hm.get(e);

      if (sm.has(e)) {
         shapeRenderer.setColor(0.0f, 0.3f, 1.0f, 0.5f);
         shapeRenderer.rect(actorPos.x - width / 2.0f, actorPos.y + 0.4f, width, height);
         if (health.health > 0.0f) {
            shapeRenderer.setColor(0.0f, 0.3f, 1.0f, 1.0f);
            shapeRenderer.rect(actorPos.x - width / 2.0f, actorPos.y + 0.4f, width * (health.health / health.maxHealth), height);
         }
      } else {
         shapeRenderer.setColor(0.0f, 1.0f, 0.0f, 0.5f);
         shapeRenderer.rect(actorPos.x - width / 2.0f, actorPos.y + 0.5f, width, height);
         if (health.health > 0.0f) {
            shapeRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            shapeRenderer.rect(actorPos.x - width / 2.0f, actorPos.y + 0.5f, width * (health.health / health.maxHealth), height);
         }
      }
   }

   @Override
   protected void end() {
      shapeRenderer.end();
   }

}