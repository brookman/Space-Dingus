package eu32k.spaceDingus.core.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.ParticleEffectComponent;

public class ParticleRenderSystem extends EntityProcessingSystem {

   private ComponentMapper<ParticleEffectComponent> pm;
   private ComponentMapper<ActorComponent> am;

   private Camera camera;
   private SpriteBatch batch;

   @SuppressWarnings("unchecked")
   public ParticleRenderSystem(Camera camera) {
      super(Aspect.getAspectForAll(ParticleEffectComponent.class, ActorComponent.class));
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      pm = world.getMapper(ParticleEffectComponent.class);
      am = world.getMapper(ActorComponent.class);
      batch = new SpriteBatch();
   }

   @Override
   protected void begin() {
      batch.setProjectionMatrix(camera.combined);
      // batch.begin();

   }

   @Override
   public void process(Entity e) {
      ParticleEffect effect = pm.get(e).effect;
      Actor actor = am.get(e).actor;

      effect.update(world.getDelta());

      Matrix4 hax = camera.combined.cpy();
      hax.translate(actor.getX(), actor.getY(), 0);
      hax.rotate(Vector3.Z, actor.getRotation());

      batch.setProjectionMatrix(hax);
      batch.begin();
      effect.draw(batch);
      batch.end();
   }

   @Override
   protected void end() {
      // batch.end();
   }

   @Override
   protected boolean checkProcessing() {
      return true;
   }
}
