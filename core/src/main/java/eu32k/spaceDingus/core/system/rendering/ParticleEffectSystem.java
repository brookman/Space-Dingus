package eu32k.spaceDingus.core.system.rendering;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.ParticleEffectComponent;

public class ParticleEffectSystem extends EntityProcessingSystem {

   private ComponentMapper<ParticleEffectComponent> pm;

   @SuppressWarnings("unchecked")
   public ParticleEffectSystem() {
      super(Aspect.getAspectForAll(ParticleEffectComponent.class));
   }

   @Override
   protected void initialize() {
      pm = world.getMapper(ParticleEffectComponent.class);

   }

   @Override
   public void process(Entity e) {
      ParticleEffect effect = pm.get(e).effect;
      effect.update(world.getDelta());
   }
}
