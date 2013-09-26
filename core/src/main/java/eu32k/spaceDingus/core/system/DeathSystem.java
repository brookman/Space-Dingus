package eu32k.spaceDingus.core.system;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.common.RemoveMarker;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;

public class DeathSystem extends EntityProcessingSystem {

   private ComponentMapper<HealthComponent> hm;
   private ComponentMapper<ShieldComponent> sm;

   @SuppressWarnings("unchecked")
   public DeathSystem() {
      super(Aspect.getAspectForAll(HealthComponent.class));
   }

   @Override
   protected void initialize() {
      hm = world.getMapper(HealthComponent.class);
      sm = world.getMapper(ShieldComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (hm.get(e).health <= 0 && !sm.has(e)) {
         RemoveMarker.markForRemovalRecursively(e);
      }
   }
}
